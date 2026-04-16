package com.Gao.dao;

import com.Gao.entity.User;
import com.Gao.util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDao {
    private static final Logger log= LoggerFactory.getLogger(UserDao.class);
    //添加用户
    public boolean addUser(User user){
        String sql="insert into user(username,password,id_card,phone,user_type) values(?,?,?,?,?)";
        try(Connection conn= DBHelper.getConnection(); PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,user.getUsername());
            ps.setString(2, user.getPassword()); //加密
            ps.setString(3,user.getId());
            ps.setString(4,user.getPhone());
            ps.setInt(5,user.getUserType());
            log.info("用户{}添加成功",user.getUsername());
            return ps.executeUpdate()>0;
        }catch (SQLException e){
            log.warn("用户{}添加错误",user.getUsername());
            log.error("执行添加sql错误",e);
            return false;
        }
    }
    //查询用户是否存在
    public boolean isExist(String username){
        String sql="select * from user where username=?";
        try(Connection conn=DBHelper.getConnection();PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,username);
            ResultSet rs=ps.executeQuery();
            log.info("查询用户{}存在",username);
            return rs.next();
        }catch (SQLException e){
            log.warn("查询用户{}未知错误",username);
            log.error("执行存在校验sql错误",e);
            return false;
        }
    }
    //通过用户名获取用户
    public User getUserWithUsername(String username){
        String sql="select * from user where username=?";
        try(Connection conn=DBHelper.getConnection();PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,username);
            ResultSet rs=ps.executeQuery();
            log.info("获取用户{}成功",username);
            if(rs.next()){
                return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("id_card"),
                        rs.getString("phone"),
                        rs.getInt("user_type")
                );
            }
        }catch (SQLException e){
            log.warn("获取用户{}错误",username);
            log.error("执行获取用户sql错误",e);
        }
        return null;
    }
    //更新密码
    public boolean updatePassword(String username,String newPassword){
        String sql="update user set password=? where username=?";
        try(Connection conn=DBHelper.getConnection();PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,newPassword);
            ps.setString(2,username);
            log.info("用户{}更新密码{}成功",username,newPassword);
            return ps.executeUpdate()>0;
        }catch (SQLException e){
            log.warn("用户{}更新密码错误",username);
            log.error("执行更新密码sql错误",e);
            return false;
        }
    }
    //验证身份
    public boolean verifyIdentity(String username,String id_card,String phone){
        String sql="select * from user where username=? and id_card=? and phone=?";
        try(Connection conn=DBHelper.getConnection();PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,username);
            ps.setString(2,id_card);
            ps.setString(3,phone);
            ResultSet rs=ps.executeQuery();
            log.info("用户{}身份验证成功",username);
            return rs.next();
        }catch (SQLException e){
            log.warn("用户{}验证身份错误",username);
            log.error("执行身份验证sql错误",e);
            return false;
        }
    }
    //验证密码
    public boolean verifyPassword(String username,String plainPassword){
        User user=getUserWithUsername(username);
        if(user==null){
            log.warn("用户{}验证密码错误",username);
            return false;
        }
        log.info("用户{}验证密码成功",username);
        return user.getPassword().equals(plainPassword);
    }
}
