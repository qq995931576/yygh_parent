package com.achang.yygh.hosp.service.impl;

import com.achang.exception.YyghException;
import com.achang.yygh.hosp.repository.DepartmentRepository;
import com.achang.yygh.hosp.service.DepartmentService;
import com.achang.yygh.model.hosp.Department;
import com.achang.yygh.vo.hosp.DepartmentQueryVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.Map;

/******
 @author 阿昌
 @create 2021-03-22 20:09
 *******
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    //上传科室
    @Override
    public void saveDepartment(Map<String, Object> parameterMap) {
        //将map集合转换成字符串
        String jsonString = JSONObject.toJSONString(parameterMap);
        //字符串 转成 对象
        Department department = JSONObject.parseObject(jsonString, Department.class);

        String hoscode = department.getHoscode();
        String depcode = department.getDepcode();

        //判断数据库中是否已经存在信息，根据hoscode,depcode查询数据库，方法命名遵循mongoDB规范，他就自动实现
        Department targetDepartment = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,depcode);

        if (targetDepartment!=null){
            //数据库已经有数据
            targetDepartment.setIsDeleted(0);
            targetDepartment.setUpdateTime(new Date());
            //保存数据，修改
            departmentRepository.save(targetDepartment);
        }else {
            //数据库没有数据
            department.setUpdateTime(new Date());
            department.setCreateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    //多条件科室分页查询
    @Override
    public Page<Department> getPage(int pageNum, int pageSize, DepartmentQueryVo departmentQueryVo) {
        //构建排序规则
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //构建分页规则
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, sort);
        //构建匹配规则
        ExampleMatcher matching = ExampleMatcher.matching();
        //改变默认字符串匹配方式：模糊查询
        ExampleMatcher stringMatcher = matching.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        //改变默认大小写忽略方式：忽略大小写
        stringMatcher.withIgnoreCase(true);

        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);

        //0为第一页
        Pageable pageable = PageRequest.of(pageNum-1, pageSize, sort);


        Example<Department> example = Example.of(department, stringMatcher);

        return departmentRepository.findAll(example, pageable);

    }

    //删除科室
    @Override
    public void delete(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department!=null){
            //departmentRepository.deleteById(department.getId());
            departmentRepository.delete(department);
        }
    }

}
