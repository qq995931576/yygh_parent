package com.achang.yygh.hosp.service.impl;

import com.achang.exception.YyghException;
import com.achang.yygh.hosp.repository.DepartmentRepository;
import com.achang.yygh.hosp.service.DepartmentService;
import com.achang.yygh.model.hosp.Department;
import com.achang.yygh.vo.hosp.DepartmentQueryVo;
import com.achang.yygh.vo.hosp.DepartmentVo;
import com.alibaba.fastjson.JSONObject;
import com.sun.corba.se.spi.ior.IdentifiableFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    //根据医院编号，查询医院所有科室列表
    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        //创建list集合，用于封装最终的数据
        ArrayList<DepartmentVo> resultList = new ArrayList<>();
        //根据医院编号，查询医院所有科室信息
        Department department = new Department();
        department.setHoscode(hoscode);
        Example<Department> example = Example.of(department);
        //所有科室列表信息
        List<Department> departmentList = departmentRepository.findAll(example);

        //根据大科室编号 分组
        Map<String, List<Department>> departmentMap = departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));
        //遍历map集合
        for (Map.Entry<String,List<Department>> entry : departmentMap.entrySet()){
            //大科室编号
            String bigCode = entry.getKey();
            //大科室编号对应的全部数据
            List<Department> bigCodeList = entry.getValue();

            //封装大科室
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepcode(bigCode);
            departmentVo.setDepname(bigCodeList.get(0).getBigname());

            //封装小科室
            List<DepartmentVo> child = new ArrayList<>();
            for (Department department1 : bigCodeList) {
                //遍历大科室数据中的所有小科室
                DepartmentVo departmentVo1 = new DepartmentVo();
                departmentVo1.setDepcode(department1.getDepcode());
                departmentVo1.setDepname(department1.getDepname());
                //封装小科室
                child.add(departmentVo1);
            }
            //把小科室list集合放到大科室child对象中
            departmentVo.setChildren(child);

            //放入最终的返回集合中
            resultList.add(departmentVo);
        }

        return resultList;
    }

    //根据医院编号、科室编号，获取科室名称
    @Override
    public String getDeptName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,depcode);
        if (department!=null){
            return department.getDepname();
        }
        return null;
    }

}
