package com.achang.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;

/******
 @author 阿昌
 @create 2021-03-20 14:08
 *******
 */
public class testEasyExcel {
    public static void main(String[] args) {
        //写地址
        String fileName = "D:\\JavaStudy\\java_src\\yygh\\test\\easyexcelTest\\01.xlsx";
        ArrayList<UserData> list = new ArrayList<>();
        for (int i = 0; i <10; i++) {
            UserData userData = new UserData();
            userData.setUsername("用户名： "+i*10);
            userData.setPassword("密码： "+i);
            list.add(userData);
        }


        EasyExcel.write(fileName,UserData.class).sheet("用户信息").doWrite(list);
    }
}
