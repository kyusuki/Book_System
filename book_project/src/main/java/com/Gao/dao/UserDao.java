package com.Gao.dao;

import com.Gao.entity.User;
import com.Gao.util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    //添加用户
    public boolean addUser(User user){
        String sql="insert into user(username,password,id_card,phone,user_type) values(?,?,?,?,?)";
        try(Connection conn= DBHelper.getConnection(); PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,user.getUsername());
            ps.setString(2, user.getPassword()); //加密
            ps.setString(3,user.getId());
            ps.setString(4,user.getPhone());
            ps.setInt(5,user.getUserType());
            return ps.executeUpdate()>0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    //查询用户是否存在
    public boolean isExist(String username){
        String sql="select * from user where username=?";
        try(Connection conn=DBHelper.getConnection();PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,username);
            ResultSet rs=ps.executeQuery();
            return rs.next();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    //通过用户名获取用户
    public User getUserWithUsername(String username){
        String sql="select * from user where username=?";
        try(Connection conn=DBHelper.getConnection();PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,username);
            ResultSet rs=ps.executeQuery();
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
            e.printStackTrace();
        }
        return null;
    }
    //更新密码
    public boolean updatePassword(String username,String newPassword){
        String sql="update user set password=? where username=?";
        try(Connection conn=DBHelper.getConnection();PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,newPassword);
            ps.setString(2,username);
            return ps.executeUpdate()>0;
        }catch (SQLException e){
            e.printStackTrace();
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
            return rs.next();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    //验证密码
    public boolean verifyPassword(String username,String plainPassword){
        User user=getUserWithUsername(username);
        if(user==null){
            return false;
        }
        return user.getPassword().equals(plainPassword);
    }
}
