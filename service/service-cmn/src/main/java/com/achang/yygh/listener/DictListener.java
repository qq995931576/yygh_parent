package com.achang.yygh.listener;

import com.achang.yygh.mapper.DictMapper;
import com.achang.yygh.model.cmn.Dict;
import com.achang.yygh.vo.cmn.DictEeVo;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/******
 @author 阿昌
 @create 2021-03-20 14:59
 *******
 */
public class DictListener extends AnalysisEventListener<DictEeVo> {

    private DictMapper mapper;

    //通过构造器获取到mapper对象操作数据库
    public DictListener(DictMapper mapper){
        this.mapper=mapper;
    }

    //一行一行读
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo,dict);
        //加入数据库
        mapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
