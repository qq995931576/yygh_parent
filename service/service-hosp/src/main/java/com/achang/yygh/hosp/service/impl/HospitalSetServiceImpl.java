package com.achang.yygh.hosp.service.impl;

import com.achang.yygh.hosp.mapper.HospitalSetMapper;
import com.achang.yygh.hosp.service.HospitalSetService;
import com.achang.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/******
 @author 阿昌
 @create 2021-03-18 16:33
 *******
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService  {

    //根据hoscode，获取SignKey
    @Override
    public String getSignKey(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        return hospitalSet.getSignKey();
    }
}
