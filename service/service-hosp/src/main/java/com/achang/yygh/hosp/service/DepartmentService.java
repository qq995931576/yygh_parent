package com.achang.yygh.hosp.service;

import com.achang.yygh.model.hosp.Department;
import com.achang.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.data.domain.Page;

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

}
