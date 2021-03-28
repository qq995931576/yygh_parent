package com.achang.yygh.hosp.service;

import com.achang.yygh.model.hosp.Schedule;
import com.achang.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/******
 @author 阿昌
 @create 2021-03-23 11:01
 *******
 */
public interface ScheduleService {
    //上传科室
    void save(Map<String, Object> parameterMap);

    //多条件分页查询
    Page<Schedule> selectPage(Integer pageNum, Integer pageSize, ScheduleQueryVo scheduleQueryVo);

    //删除科室
    void delete(String hoscode, String hosScheduleId);

    //根据【医院编号】 【科室编号】，【分页查询】排版规则数据
    Map<String, Object> getScheduleRulePage(long page, long limit, String hoscode, String depcode);


    //根据医院编号、科室编号、工作日期，查询排版详细信息
    List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate);

}
