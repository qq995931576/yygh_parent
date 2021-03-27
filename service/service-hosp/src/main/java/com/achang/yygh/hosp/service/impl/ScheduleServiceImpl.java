package com.achang.yygh.hosp.service.impl;

import com.achang.exception.YyghException;
import com.achang.utils.DateUtil;
import com.achang.yygh.hosp.repository.ScheduleRepository;
import com.achang.yygh.hosp.service.HospitalService;
import com.achang.yygh.hosp.service.HospitalSetService;
import com.achang.yygh.hosp.service.ScheduleService;
import com.achang.yygh.model.hosp.Department;
import com.achang.yygh.model.hosp.Schedule;
import com.achang.yygh.vo.hosp.BookingScheduleRuleVo;
import com.achang.yygh.vo.hosp.ScheduleQueryVo;
import com.alibaba.fastjson.JSONObject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/******
 @author 阿昌
 @create 2021-03-23 11:02
 *******
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HospitalService hospitalService;

    //上传科室
    @Override
    public void save(Map<String, Object> parameterMap) {
        //Map 转成 字符串
        String jsonString = JSONObject.toJSONString(parameterMap);
        //字符串 转成 对象
        Schedule schedule = JSONObject.parseObject(jsonString, Schedule.class);

        Schedule targetSchedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());

        if (targetSchedule==null){
            //添加
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            scheduleRepository.save(schedule);
        }else {
            //修改
            BeanUtils.copyProperties(schedule,targetSchedule);
            scheduleRepository.save(targetSchedule);
        }
    }

    //多条件分页查询
    @Override
    public Page<Schedule> selectPage(Integer pageNum, Integer pageSize, ScheduleQueryVo scheduleQueryVo) {
        //构建排序规则
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //构建分页规则,0为第一页
        PageRequest pageRequest = PageRequest.of(pageNum-1, pageSize, sort);
        //构建匹配规则
        ExampleMatcher matching = ExampleMatcher.matching();
        //改变默认字符串匹配方式：模糊查询
        ExampleMatcher stringMatcher = matching.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        //改变默认大小写忽略方式：忽略大小写
        stringMatcher.withIgnoreCase(true);

        Schedule schedule = new Schedule();
        schedule.setIsDeleted(0);
        BeanUtils.copyProperties(scheduleQueryVo,schedule);


        Example<Schedule> example = Example.of(schedule, stringMatcher);

        return scheduleRepository.findAll(example, pageRequest);
    }

    //删除科室
    @Override
    public void delete(String hoscode, String hosScheduleId) {
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (schedule!=null){
            scheduleRepository.delete(schedule);
        }
    }

    //根据【医院编号】 【科室编号】，【分页查询】排版规则数据
    @Override
    public Map<String, Object> getScheduleRulePage(long page, long limit, String hoscode, String depcode) {
        //根据医院编号、科室编号 查询对应数据
        //封装查询条件
        Criteria criteria = Criteria.where("hoscode").is(hoscode)
                .and("depcode").is(depcode);
        //根据工作日workDate期进行分组
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),//条件匹配

                Aggregation.group("workDate")//分组字段
                .first("workDate").as("workDate")//设置分组后的别名
                .count().as("docCount")//统计号源数量并设置名字
                .sum("reservedNumber").as("reservedNumber")//求和reservedNumber字段
                .sum("availableNumber").as("availableNumber"),//求和availableNumber字段

                Aggregation.sort(Sort.Direction.DESC,"workDate"),//指定是升序还是降序，指定哪个字段排序

                //分页
                Aggregation.skip((page-1)*limit),//(当前页-1)*每页记录数
                Aggregation.limit(limit)//每页显示数
        );
        //调用方法最终执行
        //参数1：上面封装的条件
        //参数2：之前的实体类
        //参数3：最后封装的尸体类
        AggregationResults<BookingScheduleRuleVo> results = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        //获取到最终的集合
        List<BookingScheduleRuleVo> ruleVoList = results.getMappedResults();

        //把日期对应的星期获取出来
        for (BookingScheduleRuleVo bookingScheduleRuleVo:ruleVoList){
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            //获取到星期几
            String week = DateUtil.getDayOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(week);
        }

        //得到分组查询后的总记录数
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),//条件查询
                Aggregation.group("workDate")//分组)
        );
        AggregationResults<BookingScheduleRuleVo> totalAggResults = mongoTemplate.aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        //获取总记录数
        int total = totalAggResults.getMappedResults().size();

        //设置最终数据进行返回
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("list",ruleVoList);
        resultMap.put("total",total);

        //根据医院编号，获取医院名称
        String hospName = hospitalService.getHospName(hoscode);

        //其他基础数据
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("hospName",hospName);

        resultMap.put("baseMap",baseMap);

        return resultMap;

    }

    /**
     * 根据日期获取周几数据
     * @param dateTime
     * @return
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }


}
