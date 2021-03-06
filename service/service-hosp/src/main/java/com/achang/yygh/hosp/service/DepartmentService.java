package com.achang.yygh.hosp.service;

import com.achang.yygh.model.hosp.Department;
import com.achang.yygh.vo.hosp.DepartmentQueryVo;
import com.achang.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/******
 @author 阿昌
 @create 2021-03-22 20:09
 *******
 */
public interface DepartmentService {
    //上传科室
    void saveDepartment(Map<String, Object> parameterMap);

    //多条件科室分页查询
    Page<Department> getPage(int pageNum, int pageSize, DepartmentQueryVo departmentQueryVo);

    //删除科室
    void delete(String hoscode, String depcode);

    ////根据医院编号，查询医院所有科室列表
    List<DepartmentVo> findDeptTree(String hoscode);

    //根据医院编号、科室编号，获取科室名称
    String getDeptName(String hoscode, String depcode);
}
