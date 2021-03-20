package com.achang.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/******
 @author 阿昌
 @create 2021-03-20 14:07
 *******
 */
@Data
public class UserData {

    @ExcelProperty("用户名")
    private String username;

    @ExcelProperty("密码")
    private String password;
}
