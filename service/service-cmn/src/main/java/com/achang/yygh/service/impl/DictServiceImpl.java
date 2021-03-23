package com.achang.yygh.service.impl;

import com.achang.exception.YyghException;
import com.achang.result.ResultCodeEnum;
import com.achang.yygh.listener.DictListener;
import com.achang.yygh.mapper.DictMapper;
import com.achang.yygh.model.cmn.Dict;
import com.achang.yygh.service.DictService;
import com.achang.yygh.vo.cmn.DictEeVo;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/******
 @author 阿昌
 @create 2021-03-18 16:33
 *******
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    //根据id查询子节点数据列表
    //keyGenerator：key的生成个规则，指定为我们上面redisconfig配置类中指定的生成规则
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    @Override
    public List<Dict> findByParentId(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Dict> dictList = baseMapper.selectList(wrapper);

        for (Dict dict : dictList) {
            Long dictId = dict.getId();
            boolean hasChild = this.hasChild(dictId);
            dict.setHasChildren(hasChild);
        }
        return dictList;
    }

    //导出数据字典接口
    @Override
    public void exportDictData(HttpServletResponse response) {
        try{
            //设置编码集
            response.setCharacterEncoding("utf-8");
            //设置输出的文件格式
            response.setContentType("application/vnd.ms-excel");
            //设置文件名
            String fileName = "dict";
            //拼接输出的文件名和文件格式
            response.setHeader("Content-disposition","attachment;filename="+fileName+".xlsx");

            //查询数据库数据
            List<Dict> dictList = baseMapper.selectList(null);
            ArrayList<DictEeVo> voArrayList = new ArrayList<>(dictList.size());
            //将查询数据库的数据封装到对应的vo类中，并装到vo集合中
            for (Dict dict : dictList) {
                DictEeVo dictEeVo = new DictEeVo();
                BeanUtils.copyProperties(dict,dictEeVo,DictEeVo.class);
                voArrayList.add(dictEeVo);
            }

            EasyExcel.write(response.getOutputStream(),DictEeVo.class)
                    .sheet("数据字典")
                    .doWrite(voArrayList);

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    //导入数据字典接口
    @Override
    //allEntries = true: 方法调用后清空所有缓存
    @CacheEvict(value = "dict", allEntries=true)
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(baseMapper))
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //根据【dictCode可选】、value获取dictName
    @Override
    public String getDictName(String dictCode, String value) {

        //判断是否有传dictCode
        if (StringUtils.isEmpty(dictCode)){
            //没传dictCode
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value",value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        }else {
            //有传dictCode
            Dict dict = this.getDictByDictcode(dictCode);
            Long parentId = dict.getId();
            Dict finalDict = baseMapper.selectOne(new QueryWrapper<Dict>().
                    eq("parent_id", parentId).
                    eq("value", value));
            return finalDict.getName();
        }


    }

    //根据dictCode获取下级节点
    @Override
    public List<Dict> findByDictCode(String dictCode) {
        //根据dictCode获取对应的id
        Dict dict = this.getDictByDictcode(dictCode);
        Long id = dict.getId();
        //根据id获取下层节点
        return this.findByParentId(id);
    }

    //根据dict_code获取对象
    private Dict getDictByDictcode(String dictCode){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code",dictCode);
        return baseMapper.selectOne(wrapper);
    }


    //判断id下面是否有子节点
    private boolean hasChild(Long id){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;
    }
}
