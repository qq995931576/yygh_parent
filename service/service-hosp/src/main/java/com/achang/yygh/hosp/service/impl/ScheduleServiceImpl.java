package com.achang.yygh.hosp.service.impl;

import com.achang.exception.YyghException;
import com.achang.yygh.hosp.repository.ScheduleRepository;
import com.achang.yygh.hosp.service.ScheduleService;
import com.achang.yygh.model.hosp.Department;
import com.achang.yygh.model.hosp.Schedule;
import com.achang.yygh.vo.hosp.ScheduleQueryVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
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

}
