package com.achang.yygh.hosp.conf;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/******
 @author 阿昌
 @create 2021-03-19 14:23
 *******
 */
@Configuration
@MapperScan("com.achang.yygh.hosp.mapper")
public class HospConfig {
    //分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
