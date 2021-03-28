package com.achang.yygh.hosp.repository;

import com.achang.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/******
 @author 阿昌
 @create 2021-03-22 20:07
 *******
 */
@Repository
public interface DepartmentRepository extends MongoRepository<Department,String> {
    //根据hoscode,depcode，查询数据库对应数据
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);

}
