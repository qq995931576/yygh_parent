package com.achang.yygh.hosp.controller;

import com.achang.result.Result;
import com.achang.yygh.hosp.service.ScheduleService;
import com.achang.yygh.model.hosp.Schedule;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/******
 @author 阿昌
 @create 2021-03-27 21:36
 *******
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/hosp/Schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    //根据【医院编号】 【科室编号】，【分页查询】排版规则数据
    @GetMapping("/getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable long page,
                                  @PathVariable long limit,
                                  @PathVariable String hoscode,
                                  @PathVariable String depcode){

        Map<String,Object> map = scheduleService.getScheduleRulePage(page,limit,hoscode,depcode);

        return Result.ok(map);

    }


}
