package com.achang.cmnClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/******
 @author 阿昌
 @create 2021-03-23 19:03
 *******
 */
@FeignClient(value = "service-cmn")
@Service
public interface DictFeignClient {
    //根据dictcode和value查询，获取数据字典名称
    @GetMapping("/admin/cmn/dict/getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,
                          @PathVariable String value);

    //根据value查询，获取数据字典名称
    @GetMapping("/admin/cmn/dict/getName//{value}")
    public String getName(@PathVariable String value);

}
