package com.achang.yygh.hosp.service.impl;

import com.achang.cmnClient.DictFeignClient;
import com.achang.yygh.hosp.repository.HospitalRepository;
import com.achang.yygh.hosp.service.HospitalService;
import com.achang.yygh.hosp.service.HospitalSetService;
import com.achang.yygh.model.hosp.Hospital;
import com.achang.yygh.vo.hosp.HospitalQueryVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
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

    @Autowired
    private DictFeignClient dictFeignClient;

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

    //根据hoscode，获取hospital对象
    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    //多条件分页查询
    @Override
    public Page<Hospital> getPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        //构建排序规则
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //构建分页规则
        PageRequest pageRequest = PageRequest.of(page-1, limit, sort);
        //构建匹配规则
        ExampleMatcher matching = ExampleMatcher.matching();
        //改变默认字符串匹配方式：模糊查询
        ExampleMatcher stringMatcher = matching.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        //改变默认大小写忽略方式：忽略大小写
        stringMatcher.withIgnoreCase(true);

        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);


        //构建规则
        Example<Hospital> example = Example.of(hospital, stringMatcher);

        Page<Hospital> all = hospitalRepository.findAll(example, pageRequest);

        all.getContent().stream().forEach(item->{
            this.setHospitalHostType(item);
        });
        return all;
    }

    //更新医院的上线状态
    @Override
    public void updateHospStatus(String id, int status) {
        //根据医院id获取对应数据
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        //设置状态值
        hospitalRepository.save(hospital);
    }

    private Hospital setHospitalHostType(Hospital hospital){
        //根据dictCode和value获取医院等级名称
        String hospitalRankName = dictFeignClient.getName("Hostype", hospital.getHostype());
        //查询省、市、地区
        String provinceName = dictFeignClient.getName(hospital.getProvinceCode());
        String districtName = dictFeignClient.getName(hospital.getDistrictCode());
        String cityName = dictFeignClient.getName(hospital.getCityCode());

        hospital.getParam().put("provinceName",provinceName);
        hospital.getParam().put("districtName",districtName);
        hospital.getParam().put("cityName",cityName);
        hospital.getParam().put("hospitalRankName",hospitalRankName);
        hospital.getParam().put("fullAddress",provinceName+districtName+cityName);

        return hospital;
    }
}
