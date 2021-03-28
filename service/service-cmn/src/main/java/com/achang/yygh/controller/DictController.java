package com.achang.yygh.controller;

import com.achang.result.Result;
import com.achang.yygh.model.cmn.Dict;
import com.achang.yygh.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/******
 @author 阿昌
 @create 2021-03-19 21:03
 *******
 */
@RestController
@RequestMapping("/admin/cmn/dict")
//@CrossOrigin
public class DictController {

    @Autowired
    private DictService dictService;

    //根据dictCode获取下级节点
    @GetMapping("/findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode){
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }

    //根据id查询子节点数据列表
    @GetMapping("/findByParentId/{id}")
    public Result findByParentId(@PathVariable Long id){
        List<Dict> list = dictService.findByParentId(id);
        return Result.ok(list);
    }

    //导出数据字典接口
    @GetMapping("/exportData")
    public Result exportData(HttpServletResponse response){
        dictService.exportDictData(response);
        return Result.ok();
    }

    //导入数据字典接口
    @PostMapping("/importData")
    public Result importData(MultipartFile file){
        dictService.importData(file);
        return Result.ok();
    }

    //根据dictcode和value查询，获取数据字典名称
    @GetMapping("/getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,
                          @PathVariable String value){
        return dictService.getDictName(dictCode,value);
    }

    //根据value查询，获取数据字典名称
    @GetMapping("/getName//{value}")
    public String getName(@PathVariable String value){
        return dictService.getDictName("",value);
    }




}
