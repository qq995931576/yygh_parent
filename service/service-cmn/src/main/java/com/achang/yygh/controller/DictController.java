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
@CrossOrigin
public class DictController {

    @Autowired
    private DictService dictService;

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


}
