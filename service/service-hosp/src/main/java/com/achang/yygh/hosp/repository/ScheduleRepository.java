package com.achang.yygh.hosp.repository;

import com.achang.yygh.model.hosp.Department;
import com.achang.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/******
 @author 阿昌
 @create 2021-03-22 20:07
 *******
 */
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule,String> {
    //根据hoscode,depcode，查询数据库对应数据
    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    //根据医院编号、科室编号、工作日期，查询排版详细信息
    List<Schedule> findScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date toDate);
}
