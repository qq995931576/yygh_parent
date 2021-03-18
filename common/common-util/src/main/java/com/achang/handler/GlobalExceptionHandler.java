package com.achang.handler;

import com.achang.exception.YyghException;
import com.achang.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/******
 @author 阿昌
 @create 2021-03-18 21:26
 *******
 */
//全局异常处理类
@ControllerAdvice
public class GlobalExceptionHandler {

    //出什么异常会执行这个方法
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    /**
     * 自定义异常处理方法
     * @param e
     * @return
     */
    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public Result error(YyghException e){
        return Result.build(e.getCode(), e.getMessage());
    }
}

