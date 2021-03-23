package com.achang.yygh.hosp.controller.api;

import com.achang.exception.YyghException;
import com.achang.helper.HttpRequestHelper;
import com.achang.result.Result;
import com.achang.result.ResultCodeEnum;
import com.achang.utils.MD5;
import com.achang.yygh.hosp.service.DepartmentService;
import com.achang.yygh.hosp.service.HospitalService;
import com.achang.yygh.hosp.service.HospitalSetService;
import com.achang.yygh.hosp.service.ScheduleService;
import com.achang.yygh.model.hosp.Department;
import com.achang.yygh.model.hosp.Hospital;
import com.achang.yygh.model.hosp.HospitalSet;
import com.achang.yygh.model.hosp.Schedule;
import com.achang.yygh.vo.hosp.DepartmentQueryVo;
import com.achang.yygh.vo.hosp.ScheduleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    //查询医院
    @PostMapping("/hospital/show")
    public Result show(HttpServletRequest request){
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> parameterMap = HttpRequestHelper.switchMap(map);

        String hoscode = (String) parameterMap.get("hoscode");
        String hospSign = (String) parameterMap.get("sign");

        if (StringUtils.isEmpty(hoscode)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        String singKeyMD5 = MD5.encrypt(signKey);
        if (!singKeyMD5.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        Hospital hospital = hospitalService.getByHoscode(hoscode);

        return Result.ok(hospital);
    }

    //上传医院
    @PostMapping("/saveHospital")
    //通过HttpServletRequest来获得从来的数据
    public Result saveHospital(HttpServletRequest request){
        //获取到传来的数据
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);

        //签名校验
        String hospKey = (String) map.get("sign");
        System.out.println(hospKey);

        //根据传递来的医院编号查询医院签名是否一致
        String hoscode = (String) map.get("hoscode");
        //根据医院编号查询数据库
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = hospitalSetService.getOne(wrapper);
        String signKey = hospitalSet.getSignKey();

        //把数据库查询签名进行MD5加密
        String md5Key = MD5.encrypt(signKey);
        System.out.println(md5Key);

//        判断传来的key和对应查到的key是否相等
        if (md5Key.equals(hospKey)){
//            调用service的方法

            //传输过程中，"+"转换成了"空格"，因为我们需要转换回来
        String logoData = (String) map.get("logoData");
        logoData = logoData.replaceAll(" ","+");
        map.put("logoData",logoData);



        hospitalService.saveHospital(map);
            return Result.ok();
        }else {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

    }

    //上传科室
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> parameterMap = HttpRequestHelper.switchMap(map);

        String hoscode = (String) parameterMap.get("hoscode");
        String sign = (String) parameterMap.get("sign");
        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMD5 = MD5.encrypt(signKey);

        if (hoscode==null){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //签名校验
        if (!signKeyMD5.equals(sign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //上传科室
        departmentService.saveDepartment(parameterMap);

        return Result.ok();

    }

    //多条件科室分页查询
    @PostMapping("/department/list")
    public Result list(HttpServletRequest request){
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> parameterMap = HttpRequestHelper.switchMap(map);

        String hoscode = (String)parameterMap.get("hoscode");
        if (hoscode==null){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        String depcode = (String)parameterMap.get("depcode");

        int pageNum = StringUtils.isEmpty(parameterMap.get("pageNum"))?1:Integer.parseInt((String)parameterMap.get("pageNum"));
        int pageSize = StringUtils.isEmpty(parameterMap.get("pageSize"))?10:Integer.parseInt((String) parameterMap.get("pageSize"));
        String hospSignKey = (String) parameterMap.get("sign");

        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMD5 = MD5.encrypt(signKey);

        //签名校验
        if (!hospSignKey.equals(signKeyMD5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setDepcode(depcode);
        departmentQueryVo.setHoscode(hoscode);

        //多条件科室分页查询
        Page<Department> pageList = departmentService.getPage(pageNum,pageSize,departmentQueryVo);

        return Result.ok(pageList);
    }

    //删除科室
    @PostMapping("department/remove")
    public Result departmentRemove(HttpServletRequest request){
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> parameterMap = HttpRequestHelper.switchMap(map);

        String hoscode = (String) parameterMap.get("hoscode");
        if (hoscode==null){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        String depcode = (String) parameterMap.get("depcode");

        String hospSignKey = (String) parameterMap.get("sign");

        String hospSetSignKey = hospitalSetService.getSignKey(hoscode);
        String hospSetSignKeyMD5 = MD5.encrypt(hospSetSignKey);

        //签名校验
        if (!hospSetSignKeyMD5.equals(hospSignKey)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //删除科室
        departmentService.delete(hoscode,depcode);

        return Result.ok();

    }

    //上传排班
    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> parameterMap = HttpRequestHelper.switchMap(map);

        String hoscode = (String) parameterMap.get("hoscode");
        //判断hoscode是否有值
        if (hoscode==null){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        String sign = (String) parameterMap.get("sign");

        //根据hoscode获取数据库中的sign
        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMD5 = MD5.encrypt(signKey);

        //数据校验
        if (!signKeyMD5.equals(sign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //上传科室
        scheduleService.save(parameterMap);


        return Result.ok();
    }

    //多条件分页查询排班
    @PostMapping("/schedule/list")
    public Result listPageSchedule(HttpServletRequest request){
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> parameterMap = HttpRequestHelper.switchMap(map);

        String hoscode = (String) parameterMap.get("hoscode");
        String depcode = (String)parameterMap.get("depcode");
        if (hoscode==null){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //页数
        Integer pageNum = StringUtils.isEmpty((Integer) parameterMap.get("pageNum"))?1:(Integer) parameterMap.get("pageNum");
        //页显示数
        Integer pageSize = StringUtils.isEmpty((Integer) parameterMap.get("pageSize"))?10:(Integer) parameterMap.get("pageSize");

        String hopSign = (String) parameterMap.get("sign");

        String hopsetSignKey = hospitalSetService.getSignKey(hoscode);
        String hopsetSignKeyMD5 = MD5.encrypt(hopsetSignKey);
        //数据校验
        if (!hopsetSignKeyMD5.equals(hopSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);

        Page<Schedule> pageModel = scheduleService.selectPage(pageNum,pageSize,scheduleQueryVo);
        return Result.ok(pageModel);
    }

    //删除科室
    @PostMapping("/schedule/remove")
    public Result scheduleRemove(HttpServletRequest request){
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> parameterMap = HttpRequestHelper.switchMap(map);

        String hoscode = (String) parameterMap.get("hoscode");
        if (hoscode==null){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        String hosScheduleId = (String) parameterMap.get("hosScheduleId");
        String sign = (String) parameterMap.get("sign");

        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMD5 = MD5.encrypt(signKey);
        //数据校验
        if (!signKeyMD5.equals(sign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //删除科室
        scheduleService.delete(hoscode,hosScheduleId);
        return Result.ok();
    }





}
