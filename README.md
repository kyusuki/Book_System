# Sql_Book - 图书管理系统（Web + Java）

[![Java](https://img.shields.io/badge/Java-17%2B-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.6-brightgreen)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-5.7%2B-green)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-brightgreen)](LICENSE)

## 项目简介

Sql_Book 是一个基于 Java + MySQL 的图书管理系统。当前版本已升级为 Spring Boot Web 应用，内置前端页面，可直接在浏览器完成登录、图书管理、借还书和分类管理。

项目仍保留早期控制台代码，便于学习 JDBC 与业务逻辑设计；默认推荐使用 Web 入口运行。

## 本次更新重点

- 新增 Web 前端页面（登录/注册/忘记密码/图书管理/分类管理/借阅统计）
- 新增基于 Session 的登录态校验与接口拦截
- 新增图书、分类、借阅记录 REST API
- 前后端统一使用 JSON 响应结构，便于联调与扩展
- 保留原有 BookManager、DAO、实体层逻辑

## 功能清单

### 认证与账号

- 验证码登录
- 用户注册
- 忘记密码重置
- 会话登录态检查（/api/auth/me）
- 退出登录

### 图书管理

- 图书列表查询
- 按 ISBN / 书名 / 分类搜索
- 新增图书（管理员）
- 修改图书（管理员）
- 删除图书（管理员）

### 借阅业务

- 借书
- 还书
- 借阅记录查询
- 图书统计（总种数、有库存种数、借空种数）

### 分类管理

- 分类列表查询
- 新增分类（管理员）
- 修改分类（管理员）
- 删除分类（管理员，且分类下无图书）

## 角色权限

- 管理员（user_type=1）
  - 图书和分类的增删改
  - 可代他人借书、代还书
  - 查看全部借阅记录
- 普通用户（user_type=0）
  - 浏览图书、搜索图书
  - 借书和还书（基于本人身份）
  - 查看自己的借阅记录

## 技术栈

- 后端
  - Spring Boot 3.3.6
  - Spring Web
  - Spring JDBC
  - HikariCP
- 前端
  - 原生 HTML + CSS + JavaScript（无框架）
- 数据库
  - MySQL 5.7+
- 构建
  - Maven

## 项目结构

```text
Sql_Book/
├── MySQL_statements.txt
├── README.md
└── book_project/
    ├── pom.xml
    ├── src/main/java/com/Gao/
    │   ├── ProjectWebApplication.java      # Web 启动入口
    │   ├── dao/
    │   ├── entity/
    │   ├── util/
    │   ├── view/                           # 早期控制台入口与交互逻辑
    │   └── web/
    │       ├── config/                     # 拦截器与 MVC 配置
    │       ├── controller/                 # Web API 控制器
    │       ├── dto/
    │       └── service/
    └── src/main/resources/
        ├── application.properties
        ├── application.yaml
        ├── db.properties
        └── static/
            ├── index.html                  # 前端页面
            ├── app.js                      # 前端逻辑
            └── styles.css
```

## 运行环境

- JDK 17 及以上（推荐与 pom 对齐）
- Maven 3.8+
- MySQL 5.7+

可使用以下命令确认环境：

```bash
java -version
mvn -version
mysql --version
```

## 快速开始

### 1. 初始化数据库

在 MySQL 中执行根目录 SQL 脚本：

```bash
mysql -u root -p < MySQL_statements.txt
```

脚本会创建并使用数据库 book_system，并初始化用户、图书、分类等表结构与示例数据。

### 2. 修改数据库连接

主要配置位置：

- book_project/src/main/resources/application.yaml
- book_project/src/main/resources/db.properties

默认示例（按本机修改）：

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/book_system?useUnicode=true&characterEncoding=utf8&autoReconnectForPools=true&useSSL=false
    username: root
    password: 666666
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 3. 启动项目（Web 模式）

进入子项目目录并启动：

```bash
cd book_project
mvn spring-boot:run
```

默认端口：9098

浏览器访问：

- 首页：http://localhost:9098/
- 前端会调用同源 API：http://localhost:9098/api

### 4. 可选：运行控制台版本

如果需要体验旧版控制台流程：

```bash
cd book_project
mvn exec:java -Dexec.mainClass="com.Gao.view.App"
```

## 主要 API 概览

### 认证

- GET /api/auth/captcha
- POST /api/auth/login
- POST /api/auth/logout
- GET /api/auth/me
- POST /api/auth/register
- POST /api/auth/forgot-password

### 图书与借阅

- GET /api/books
- GET /api/books/search?mode=isbn|title|category&keyword=...
- POST /api/books
- PUT /api/books/{isbn}
- DELETE /api/books/{isbn}
- POST /api/books/borrow
- POST /api/books/return
- GET /api/books/stats
- GET /api/borrow-records

### 分类

- GET /api/categories
- POST /api/categories
- PUT /api/categories/{id}
- DELETE /api/categories/{id}

说明：除验证码、登录、注册、忘记密码外，其余 /api 接口均需登录后访问。

## 预置账号

SQL 脚本默认初始化两个管理员账号：

- gyf / 111111
- dashuaige / 666666

普通用户可通过前端注册页面自行创建。

## 常见问题

- 访问首页报错或空白
  - 检查是否从 ProjectWebApplication 启动
  - 检查端口 9098 是否被占用
- 登录后接口返回未授权
  - 确认浏览器允许携带 Cookie（前端使用 credentials: include）
- 数据库连接失败
  - 检查 application.yaml 与 db.properties 中的账号密码
  - 确认 MySQL 服务已启动，数据库名为 book_system

## 已知限制

- 当前密码仍为明文存储，不适合生产环境
- 暂未提供系统化单元测试/集成测试
- 前端未接入复杂组件库，适合教学与演示场景

## 后续建议

- 接入 BCrypt 密码加密
- 增加 JUnit + MockMvc 测试
- 增加分页、排序、导出等管理能力
- 增加操作审计日志与权限细分

## 许可证

本项目采用 MIT 协议，详见 LICENSE。
