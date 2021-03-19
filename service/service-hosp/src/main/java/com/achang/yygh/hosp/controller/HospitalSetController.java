package com.achang.yygh.hosp.controller;

import com.achang.result.Result;
import com.achang.utils.MD5;
import com.achang.yygh.hosp.service.HospitalSetService;
import com.achang.yygh.model.hosp.HospitalSet;
import com.achang.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/******
 @author 阿昌
 @create 2021-03-18 16:52
 *******
 */
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    //查询医院设置表的所有信息
    @GetMapping("/getAll")
    public Result getAll(){
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    //逻辑删除医院
    @DeleteMapping("/deleteHospitalById/{id}")
    public Result deleteHospitalById(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);
        if (flag){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //多条件分页查询
    @PostMapping("/findPageCondition/{page}/{limit}")
    public Result findPageCondition(@PathVariable long page,
                                  @PathVariable long limit,
                                  @RequestBody(required = false)HospitalSetQueryVo hospitalSetQueryVo){
        Page<HospitalSet> hospitalSetPage = new Page<>(page, limit);
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hoscode = hospitalSetQueryVo.getHoscode();
        String hosname = hospitalSetQueryVo.getHosname();

        //判断是否有传入医院编号
        if (!StringUtils.isEmpty(hoscode)){
            wrapper.eq("hoscode",hoscode);
        }
        //判断是否有传入医院名称
        if (!StringUtils.isEmpty(hosname)){
            wrapper.like("hosname",hosname);
        }
        //排序
        wrapper.orderByDesc("create_time");

        //分页查询
        Page<HospitalSet> pageHospitalSet  = hospitalSetService.page(hospitalSetPage, wrapper);

        return Result.ok(pageHospitalSet);
    }

    //添加医院设置
    @PostMapping("addHospitalSet")
    public Result addHospitalSet(@RequestBody HospitalSet hospitalSet){
        //设置状态： 1使用 ， 0不能使用
        hospitalSet.setStatus(1);
        //设置签名秘钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));

        boolean flag = hospitalSetService.save(hospitalSet);
        if (flag){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //根据id获取医院设置
    @GetMapping("/getHospitalSetById/{id}")
    public Result getHospitalSetById(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    //修改医院设置
    @PostMapping("/updateHospital")
    public Result updateHospital(@RequestBody HospitalSet hospitalSet){
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //批量删除医院设置
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Long> Ids){
        boolean flag = hospitalSetService.removeByIds(Ids);
        if (flag){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //医院设置锁定和解锁
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,@PathVariable Integer status){
        //根据id查询出医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //修改信息
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }


    //发送签名秘钥
    @PutMapping("/sendKey/{id}")
    public Result sendKey(@PathVariable Long id){
        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //获取对应的秘钥
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO 发送短信
        return Result.ok();
    }


}
