package com.achang.yygh.hosp.service;

import com.achang.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.IService;

/******
 @author 阿昌
 @create 2021-03-18 16:31
 *******
 */
public interface HospitalSetService extends IService<HospitalSet> {
    //根据hoscode，获取SignKey
    String getSignKey(String hoscode);
}
