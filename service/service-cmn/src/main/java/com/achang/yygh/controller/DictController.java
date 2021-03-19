package com.achang.yygh.controller;

import com.achang.yygh.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/******
 @author 阿昌
 @create 2021-03-19 21:03
 *******
 */
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {
    @Autowired
    private DictService dictService;


}
