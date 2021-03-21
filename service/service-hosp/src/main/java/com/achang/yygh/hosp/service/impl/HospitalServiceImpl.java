package com.achang.yygh.hosp.service.impl;

import com.achang.yygh.hosp.repository.HospitalRepository;
import com.achang.yygh.hosp.service.HospitalService;
import com.achang.yygh.hosp.service.HospitalSetService;
import com.achang.yygh.model.hosp.Hospital;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

/******
 @author 阿昌
 @create 2021-03-21 20:37
 *******
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    //上传医院接口
    @Override
    public void saveHospital(Map<String, Object> map) {
        //把map集合转化成对象 Hospital
        //先转成 Map -> 字符串
        String mapString = JSONObject.toJSONString(map);
        //将字符串 -> 对象
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        //获取到医院秘钥
        String hoscode = hospital.getHoscode();
        //根据秘钥获取，判断值是否存在，他会很智能的感觉方法命名来自动实现
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);

        //如果存在，进行修改
        if (hospitalExist!=null){
            //设置状态
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            //修改操作
            hospitalRepository.save(hospital);
        }else {
            //不存在，进行添加
            //设置状态
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            //修改操作
            hospitalRepository.save(hospital);

        }
    }
}
