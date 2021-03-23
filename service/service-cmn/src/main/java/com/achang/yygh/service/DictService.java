package com.achang.yygh.service;

import com.achang.yygh.model.cmn.Dict;
import com.achang.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/******
 @author 阿昌
 @create 2021-03-18 16:31
 *******
 */
public interface DictService extends IService<Dict> {
    ////根据id查询子节点数据列表
    List<Dict> findByParentId(Long id);

    //导出数据字典接口
    void exportDictData(HttpServletResponse response);

    //导入数据字典接口
    void importData(MultipartFile file);

    //根据【dictCode可选】、value获取dictName
    String getDictName(String dictCode, String value);

    //根据dictCode获取下级节点
    List<Dict> findByDictCode(String dictCode);

}
