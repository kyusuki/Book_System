package com.Gao.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.*;
import java.io.InputStream;
import java.util.Properties;

public class DBHelper {
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
            e.printStackTrace();
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
