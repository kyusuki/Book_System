package com.Gao.view;

import com.Gao.entity.User;
import com.Gao.util.ClearScreen;

import java.util.Scanner;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger log= LoggerFactory.getLogger(App.class);
    public static void main(String[] args){
        System.out.println("欢迎使用学生管理系统");
        ClearScreen cs=new ClearScreen(); //声明清屏工具
        ArrayList<User> userList=new ArrayList<>(); //全局用户列表
        Scanner sc=new Scanner(System.in); //全局扫描器
        Login l=new Login(); //只创建一次Login对象
        while(true){
            cs.cleanScreen(); //清屏
            System.out.println("请选择：1.登录 2.注册 3.退出");
            String choice=sc.next();
            switch(choice){
                case "1"->{
                    //while(true){
                    User u=l.login(sc);
                    log.info("用户{}执行登录操作",u.getUsername());
                    if(u!=null){
                        BookTest bt=new BookTest(sc); //创建BookTest对象
                        bt.test(u); //进入图书管理系统
                    }
                    else{
                        log.warn("用户{}似乎不存在或密码错误，执行二次提示",u.getUsername());
                        System.out.println("1.重新登录 2.忘记密码 3.退出");
                        String choice2=sc.next();
                        switch(choice2){
                            case "1"->{
                                break;
                            }
                            case "2"->{
                                log.info("用户{}执行忘记密码操作",u.getUsername());
                                Forget f=new Forget(); //创建Forget对象
                                f.forget(sc); //调用忘记密码方法
                                break;
                            }
                            case "3"->{
                                log.info("用户{}似乎放弃了思考",u.getUsername());
                                System.exit(0);
                            }
                            default->System.out.println("输入错误");
                        }
                    }
                    //}
                }
                case "2"->{
                    log.info("新用户注册");
                    Register r=new Register(); //创建Register对象
                    r.register(sc); //调用注册方法
                }
                case "3"->{
                    log.info("用户已退出使用或放弃使用");
                    System.out.println("退出系统");
                    sc.close();
                    System.exit(0);
                }
                default->System.out.println("输入错误");
            }
        }
    }
}

