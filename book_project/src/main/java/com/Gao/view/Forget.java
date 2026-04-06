package com.Gao.view;

import com.Gao.dao.UserDao;

import java.util.Scanner;

public class Forget {
    private UserDao userDao=new UserDao();
    public void forget(Scanner sc){
        System.out.println();
        System.out.println("请输入用户名：");
        String username=sc.next();
        System.out.println();
        System.out.println("请输入身份证号码：");
        String id1=sc.next();
        System.out.println();
        System.out.println("请输入手机号码：");
        String phone1=sc.next();
        System.out.println();
        if(userDao.verifyIdentity(username,id1,phone1)){
            System.out.println("请输入新密码：");
            String newPassword=sc.next();
            if(userDao.updatePassword(username,newPassword)){
                System.out.println("密码修改成功");
            }
            else{
                System.out.println("密码修改失败");
            }
        }
        else{
            System.out.println("账号信息不一致");
        }
    }
}
