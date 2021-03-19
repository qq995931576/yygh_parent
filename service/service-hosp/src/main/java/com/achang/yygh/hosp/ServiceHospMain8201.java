package com.achang.yygh.hosp;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/******
 @author 阿昌
 @create 2021-03-18 16:05
 *******
 */
@SpringBootApplication

@ComponentScan("com.achang")//扫描swagger
public class ServiceHospMain8201 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospMain8201.class,args);
    }
}
