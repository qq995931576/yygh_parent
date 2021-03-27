package com.achang.yygh.hosp.controller;

import com.achang.result.Result;
import com.achang.yygh.hosp.service.DepartmentService;
import com.achang.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/******
 @author 阿昌
 @create 2021-03-26 20:25
 *******
 */
@RestController
@RequestMapping("/admin/hosp/department")
@CrossOrigin
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    //根据医院编号，查询医院所有科室列表
        @GetMapping("/getDeptList/{hoscode}")
    public Result getDeptList(@PathVariable String hoscode){
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }



}
