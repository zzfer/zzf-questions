# 数据库初始化指南

本项目提供了智能的数据库初始化系统，可以在应用启动时自动检查数据库状态并进行相应的初始化操作。

## 🚀 快速开始

### 自动初始化（推荐）

应用启动时会自动执行以下检查和初始化：

1. **表结构初始化**：通过 `init.sql` 脚本自动创建表结构
2. **数据检查**：`DatabaseInitializer` 会检查数据库是否为空
3. **示例数据**：如果数据库为空，自动插入示例数据

### 手动控制

通过 API 接口可以手动控制数据库初始化：

```bash
# 检查数据库状态
GET /api/admin/database/status

# 获取统计信息
GET /api/admin/database/stats

# 初始化示例数据
POST /api/admin/database/init-sample

# 强制重新初始化（清空后重新初始化）
POST /api/admin/database/force-reinit

# 清空所有数据
DELETE /api/admin/database/clear-all
```

## ⚙️ 配置选项

### application.properties 配置

```properties
# 数据库初始化配置
# 可选值: always(总是执行), embedded(仅嵌入式数据库), never(从不执行)
spring.sql.init.mode=embedded
spring.sql.init.schema-locations=classpath:sql/init.sql

# 可选：数据初始化脚本
# spring.sql.init.data-locations=classpath:sql/data.sql

# JPA配置
spring.jpa.hibernate.ddl-auto=create-drop
```

### 配置说明

| 配置项 | 说明 | 推荐值 |
|--------|------|--------|
| `spring.sql.init.mode` | SQL脚本执行模式 | `embedded` |
| `spring.jpa.hibernate.ddl-auto` | DDL自动执行模式 | `create-drop`(开发), `validate`(生产) |
| `spring.sql.init.schema-locations` | 表结构脚本位置 | `classpath:sql/init.sql` |
| `spring.sql.init.data-locations` | 初始数据脚本位置 | 可选 |

## 📁 文件结构

```
src/main/
├── java/com/exam/config/
│   ├── DatabaseInitializer.java      # 启动时自动初始化
│   └── DatabaseConfig.java           # 数据库配置类
├── java/com/exam/service/
│   ├── DatabaseInitService.java      # 初始化服务接口
│   └── impl/DatabaseInitServiceImpl.java # 初始化服务实现
├── java/com/exam/controller/
│   └── DatabaseController.java       # 数据库管理API
└── resources/sql/
    ├── init.sql                       # 表结构脚本
    └── data.sql                       # 可选的初始数据脚本
```

## 🔧 使用场景

### 1. 开发环境

```properties
# 开发环境配置
spring.profiles.active=dev
spring.sql.init.mode=always
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

- 每次启动都重新创建表结构
- 自动初始化示例数据
- 显示SQL日志便于调试

### 2. 测试环境

```properties
# 测试环境配置
spring.profiles.active=test
spring.sql.init.mode=embedded
spring.jpa.hibernate.ddl-auto=create-drop
```

- 使用内存数据库
- 每次测试后清理数据
- 快速启动和关闭

### 3. 生产环境

```properties
# 生产环境配置
spring.profiles.active=prod
spring.sql.init.mode=never
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
```

- 不自动执行SQL脚本
- 仅验证表结构
- 关闭SQL日志
- 使用持久化数据库

## 🛠️ 高级功能

### 1. 条件初始化

`DatabaseInitializer` 会智能检查：
- 数据库是否为空
- 是否需要初始化示例数据
- 避免重复初始化

### 2. 环境感知

`DatabaseConfig` 提供环境特定的配置：
- 开发环境：详细日志
- 测试环境：快速清理
- 生产环境：安全配置

### 3. API管理

通过 `DatabaseController` 提供：
- 状态检查
- 手动初始化
- 数据清理
- 统计信息

## 📊 API 示例

### 检查数据库状态

```bash
curl -X GET http://localhost:9090/api/admin/database/status
```

响应：
```json
{
  "success": true,
  "data": {
    "initialized": true,
    "questionCount": 3,
    "expenseCount": 0,
    "optionCount": 12,
    "empty": false
  }
}
```

### 初始化示例数据

```bash
curl -X POST http://localhost:9090/api/admin/database/init-sample
```

### 强制重新初始化

```bash
curl -X POST http://localhost:9090/api/admin/database/force-reinit
```

⚠️ **警告**：这将清空所有现有数据！

## 🔍 故障排除

### 常见问题

1. **表已存在错误**
   - 检查 `spring.jpa.hibernate.ddl-auto` 配置
   - 使用 `create-drop` 或 `create` 模式

2. **数据重复初始化**
   - `DatabaseInitializer` 会自动检查避免重复
   - 可通过API手动控制

3. **权限问题**
   - 确保数据库用户有创建表的权限
   - 检查数据库连接配置

### 日志分析

启动时查看日志：
```
=== 数据库配置信息 ===
数据源URL: jdbc:mysql://localhost:3306/zzf_questions
DDL模式: create-drop
SQL初始化模式: embedded
========================
开始检查数据库初始化状态...
当前题目数量: 0
检测到题目表为空，开始初始化示例题目数据...
已创建 3 道示例题目
数据库初始化检查完成！
```

## 📝 最佳实践

1. **开发阶段**：使用自动初始化，快速搭建开发环境
2. **测试阶段**：每次测试前清理数据，确保测试独立性
3. **生产部署**：关闭自动初始化，手动管理数据库
4. **数据备份**：生产环境定期备份，避免意外数据丢失
5. **版本控制**：将SQL脚本纳入版本控制，跟踪数据库变更

## 🔗 相关文档

- [API文档](API_DOCUMENTATION.md)
- [项目README](README.md)
- [Spring Boot数据库初始化官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization)