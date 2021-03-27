package com.achang.yygh.hosp.service;

import com.achang.yygh.model.hosp.Hospital;
import com.achang.yygh.model.hosp.HospitalSet;
import com.achang.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/******
 @author 阿昌
 @create 2021-03-21 20:37
 *******
 */
public interface HospitalService {
    //上传医院接口
    void saveHospital(Map<String, Object> map);

    //查询医院
    Hospital getByHoscode(String hoscode);

    //多条件分页查询
    Page<Hospital> getPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    //更新医院的上线状态
    void updateHospStatus(String id, int status);

    //根据id获取医院数据
    Map<String,Object> getById(String id);

    //根据医院编号，获取医院名称
    String getHospName(String hoscode);
}
