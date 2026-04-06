package com.Gao.util;

import com.Gao.dao.UserDao;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBHelper {
    private static final Logger log= LoggerFactory.getLogger(DBHelper.class);
    //添加连接池做链接优化
    private static HikariDataSource dataSource;
    static{
        try{
            InputStream is=DBHelper.class.getResourceAsStream("/db.properties");
            Properties properties=new Properties();
            properties.load(is);
            HikariConfig config=new HikariConfig();
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.username"));
            config.setPassword(properties.getProperty("db.password"));
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000);
            dataSource=new HikariDataSource(config); //创建连接池
        }catch (Exception e){
//            e.printStackTrace();
            log.error("数据库连接错误",e);
        }
    }
    public static Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
}
