package com.achang.yygh.hosp.controller.api;

import com.achang.helper.HttpRequestHelper;
import com.achang.result.Result;
import com.achang.yygh.hosp.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/******
 @author 阿昌
 @create 2021-03-21 20:42
 *******
 */
@RestController
@RequestMapping("/api/hosp/")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    //上传医院
    @PostMapping("/saveHospital")
    //通过HttpServletRequest来获得从来的数据
    public Result saveHospital(HttpServletRequest request){
        //获取到传来的数据
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        //调用service的方法
        hospitalService.saveHospital(map);
        return Result.ok();

    }

}
