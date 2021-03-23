package com.achang.yygh.hosp.controller;

import com.achang.result.Result;
import com.achang.yygh.hosp.service.HospitalService;
import com.achang.yygh.model.hosp.Hospital;
import com.achang.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/******
 @author 阿昌
 @create 2021-03-23 13:52
 *******
 */
@RestController
@RequestMapping("/admin/hosp/hospital")
@CrossOrigin
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;


    //多条件分页查询
    @GetMapping("/pageList/{page}/{limit}")
    public Result pageList(@PathVariable Integer page,
                           @PathVariable Integer limit,
                            HospitalQueryVo hospitalQueryVo){
        Page<Hospital> pageList = hospitalService.getPage(page,limit,hospitalQueryVo);
        return Result.ok(pageList);
    }


}
