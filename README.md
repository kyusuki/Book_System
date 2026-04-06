# Sql_Book - 图书管理系统

[![Java Version](https://img.shields.io/badge/Java-25-blue)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-5.7+-green)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.x-orange)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-brightgreen)](LICENSE)

## 📖 项目简介

**Sql_Book** 是一款基于 Java 和 MySQL 的图书管理系统，是学习 JDBC 与 SQL 操作的优秀实践项目。

### 核心特点

- 🎯 **原生 SQL 设计**：采用原生 JDBC 实现，不依赖 ORM 框架，深入理解 SQL 执行流程
- 👥 **双角色管理**：支持管理员和普通用户两种角色，权限管理清晰
- 🔄 **事务处理**：借阅/还书操作使用数据库事务保证数据一致性
- 💾 **连接池技术**：集成 HikariCP 提升数据库连接效率
- 📚 **完整示例数据**：提供初始化脚本和示例数据快速上手

### 学习价值

本项目适合以下学习场景：
- Java 开发者深入学习 JDBC 与数据库连接管理
- SQL 实践学习，理解持久层设计模式
- 完整的控制台应用开发流程
- 真实场景的业务逻辑实现

**局限性说明**：当前版本采用明文密码存储、缺少单元测试。建议在生产环境中增加密码加密、安全验证及完整的测试覆盖。

---

## ✨ 功能特性

| 功能模块 | 具体功能 | 说明 |
|--------|--------|------|
| **用户管理** | 用户注册 | 新用户注册账户 |
| | 用户登录 | 支持管理员和普通用户登录 |
| | 密码重置 | 用户自助密码重置 |
| **图书管理** | 增删改查 | 新增、删除、修改、查询图书信息 |
| | 图书搜索 | 按 ISBN、标题、作者等条件搜索 |
| | 库存统计 | 实时统计图书库存情况 |
| **借阅系统** | 借书功能 | 用户按 ISBN 借阅图书 |
| | 还书功能 | 用户归还借阅的图书 |
| | 记录查询 | 查看借阅历史记录 |
| **数据库集成** | MySQL 支持 | 完整的关系型数据库集成 |
| | 事务处理 | 重要操作使用事务保证数据完整性 |

### 可优化方向

- 🔒 **安全增强**：添加密码加密（BCrypt/MD5）、权限验证
- 🧪 **测试覆盖**：编写 JUnit 单元测试和集成测试
- 📝 **日志系统**：已实现基础的 SLF4J 日志查询
- 🎨 **界面优化**：升级到图形界面(Swing/JavaFX)
- 📊 **功能扩展**：图书分类、借阅统计分析等

---

## 🛠️ 环境要求

### 必需环境
- **Java 8** 及以上（推荐 **Java 25**）
- **MySQL 5.7** 及以上
- **Maven 3.x** 及以上

### 推荐工具
- **IDE**：IntelliJ IDEA 或 Eclipse
- **数据库工具**：MySQL Workbench、Navicat

### 验证环境
```bash
java -version
mvn --version
mysql --version
```

---

## 🚀 安装与运行

### 1. 克隆或下载项目

```bash
# 使用 Git 克隆
git clone <repository-url>
cd Sql_Book/book_project

# 或直接下载 ZIP 文件后解压
```

### 2. 数据库配置

执行 `MySQL_statements.txt` 文件中的 SQL 语句创建数据库、表和示例数据：

```bash
# 使用 MySQL 命令行工具
mysql -u root -p < MySQL_statements.txt

# 或在 MySQL Workbench 中执行脚本
```

**数据库初始化包含的操作：**
- 创建数据库 `sql_book`
- 创建 `users`、`books`、`borrow_records` 等表
- 插入示例管理员和用户数据
- 插入示例图书数据

### 3. 修改数据库配置

编辑 `src/main/resources/db.properties` 文件，填入您的 MySQL 连接信息：

```properties
# 默认配置（需根据实际环境修改）
db.driver=com.mysql.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/sql_book
db.user=root
db.password=666666
db.pool.size=10
```

### 4. 构建项目

```bash
# 清理并编译项目
mvn clean compile

# 生成项目包（可选）
mvn package
```

### 5. 运行程序

**方式一：使用 Maven 运行**
```bash
mvn exec:java -Dexec.mainClass="com.Gao.view.App"
```

**方式二：使用 IDE 运行**
- 在 IntelliJ IDEA 中：打开 `App.java`，右击选择 `Run 'App.main()'`
- 在 Eclipse 中：右击项目选择 `Run As` > `Java Application`

**方式三：使用 JAR 包运行**
```bash
mvn package
java -cp target/book_project-1.0-SNAPSHOT.jar com.Gao.view.App
```

### 🔧 常见问题排查

| 问题 | 解决方案 |
|-----|--------|
| **数据库连接失败** | ✓ 检查 MySQL 服务是否启动<br/>✓ 验证 db.properties 中的连接信息<br/>✓ 确认数据库用户名密码正确 |
| **端口 3306 被占用** | ✓ 修改 MySQL 端口或检查是否有其他服务占用<br/>✓ 在 db.properties 中修改连接端口 |
| **ClassNotFoundException** | ✓ 执行 `mvn clean install` 下载依赖<br/>✓ 检查 MySQL JDBC 驱动是否在 pom.xml 中 |
| **Permission denied** | ✓ 检查 MySQL 用户权限<br/>✓ 确保用户有数据库读写权限 |

---

## 📖 使用说明

### 程序流程

1. **启动程序**：运行 `App.java` 后显示登录菜单
2. **用户登录**：输入用户名和密码
3. **角色选择**：管理员和普通用户享受不同的功能菜单
4. **菜单操作**：根据角色通过菜单选项进行相应操作
5. **退出程序**：选择"退出"选项结束程序

### 预置账户

| 账户类型 | 用户名 | 密码 | 说明 |
|--------|--------|------|------|
| 管理员 | `gyf` | `111111` | 拥有图书管理权限 |
| 普通用户 | `user1` | `user123` | 仅有借阅权限 |

### 示例操作流程

**场景：普通用户借阅图书**

```
欢迎使用图书管理系统
================================
1. 用户登录
2. 管理员登录
3. 注册新用户
4. 退出系统
请选择: 1

请输入用户名: user1
请输入密码: user123

用户菜单
================================
1. 查看可借图书
2. 借书 (需输入 ISBN)
3. 还书 (需输入 ISBN)
4. 查看借阅记录
5. 退出登录
请选择: 2

请输入图书 ISBN: 978-7-111-xxxxx
借书成功！
```

---

## 📁 项目结构

```
Sql_Book/
├── MySQL_statements.txt          # 数据库初始化脚本
├── book_project/                 # 主项目目录
│   ├── pom.xml                   # Maven 配置文件
│   │
│   ├── src/main/java/com/Gao/
│   │   ├── entity/               # 实体类包
│   │   │   ├── Book.java         # 图书实体
│   │   │   ├── User.java         # 用户实体
│   │   │   └── BorrowRecord.java # 借阅记录实体
│   │   │
│   │   ├── dao/                  # 数据访问层
│   │   │   ├── BookDAO.java      # 图书数据操作
│   │   │   ├── UserDAO.java      # 用户数据操作
│   │   │   └── BorrowRecordDAO.java # 借阅记录操作
│   │   │
│   │   ├── util/                 # 工具类包
│   │   │   ├── DBHelper.java     # 数据库连接助手
│   │   │   ├── DBConfig.java     # 数据库配置管理
│   │   │   └── PasswordUtil.java # 密码工具（可扩展）
│   │   │
│   │   └── view/                 # 视图/控制层
│   │       ├── App.java          # 程序入口
│   │       ├── LoginView.java    # 登录界面
│   │       ├── AdminView.java    # 管理员菜单
│   │       ├── UserView.java     # 用户菜单
│   │       └── BookManager.java  # 图书管理相关业务逻辑
│   │
│   └── src/main/resources/
│       └── db.properties         # 数据库配置文件
│
└── README.md                      # 本文件
```

### 关键包说明

| 包名 | 职责 | 示例 |
|-----|------|------|
| **entity** | 数据模型，映射数据库表 | `Book` 对应 `books` 表 |
| **dao** | 数据访问层，负责 CRUD 操作 | 执行 SQL 查询、插入、更新、删除 |
| **util** | 工具类，提供通用功能 | 数据库连接、配置管理 |
| **view** | 视图层，用户交互界面 | 菜单显示、数据输入、结果输出 |

### 核心类说明

- **DBHelper.java**：管理数据库连接，使用 HikariCP 连接池
- **BookDAO.java**：实现图书的增删改查、搜索、库存统计等操作
- **UserDAO.java**：管理用户登录、注册、密码重置等操作
- **App.java**：程序主入口，控制整个应用流程

---

## 🤝 贡献指南

感谢您对本项目的关注！欢迎通过以下方式贡献代码和建议。

### 如何贡献

1. **Fork 项目**
   ```bash
   点击 GitHub 上的 "Fork" 按钮
   ```

2. **创建分支**
   ```bash
   git clone https://github.com/your-username/Sql_Book.git
   cd Sql_Book
   git checkout -b feature/your-feature-name
   ```

3. **提交更改**
   ```bash
   git add .
   git commit -m "Add your feature description"
   ```

4. **提交 Pull Request**
   ```bash
   git push origin feature/your-feature-name
   ```
   然后在 GitHub 上创建 Pull Request

### 改进建议

欢迎以下方面的改进建议和 PR：

- 🧪 **添加 JUnit 单元测试**：提升代码质量和可维护性
- 📝 **集成日志系统**：改进 SLF4J 记录应用日志
- 🔒 **增强安全性**：
  - 实现密码加密（BCrypt、MD5）
  - 添加权限控制机制
  - SQL 注入防护
- 🎨 **界面升级**：使用 Swing 或 JavaFX 构建图形界面
- 📊 **功能扩展**：
  - 添加图书分类功能
  - 借阅统计分析
  - 用户行为追踪
- 📚 **文档完善**：改进 API 文档和使用说明

### 代码规范

- 遵循 Google Java 代码风格指南
- 使用有意义的变量名和方法名
- 为复杂逻辑添加注释
- 提交前运行 `mvn clean compile` 确保编译通过

---

## 📄 许可证

本项目采用 **MIT 协议**，详见 [LICENSE](LICENSE) 文件。

---
