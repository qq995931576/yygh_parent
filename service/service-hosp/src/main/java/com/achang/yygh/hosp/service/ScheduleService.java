package com.achang.yygh.hosp.service;

import com.achang.yygh.model.hosp.Schedule;
import com.achang.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

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

}
