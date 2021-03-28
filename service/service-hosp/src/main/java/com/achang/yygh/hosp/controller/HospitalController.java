package com.achang.yygh.hosp.controller;

import com.achang.result.Result;
import com.achang.yygh.hosp.service.HospitalService;
import com.achang.yygh.model.hosp.Hospital;
import com.achang.yygh.vo.hosp.HospitalQueryVo;
import com.sun.corba.se.spi.presentation.rmi.IDLNameTranslator;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/******
 @author 阿昌
 @create 2021-03-23 13:52
 *******
 */
@RestController
@RequestMapping("/admin/hosp/hospital")
//@CrossOrigin
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

    //更新医院的上线状态
    @GetMapping("/updateHospStatus/{id}/{status}")
    public Result updateHospStatus(@PathVariable String id, @PathVariable int status){
        hospitalService.updateHospStatus(id,status);
        return Result.ok();
    }

    //根据医院id，获取医院数据
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable String id){
        Map<String, Object> map = hospitalService.getById(id);
        return Result.ok(map);
    }


}
