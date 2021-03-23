package com.achang.yygh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/******
 @author 阿昌
 @create 2021-03-19 20:54
 *******
 */
@SpringBootApplication
@ComponentScan("com.achang")
@EnableDiscoveryClient //开启服务发现
public class ServiceCmnMain8205 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmnMain8205.class,args);
    }
}
