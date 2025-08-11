# 题目管理系统 API 文档

## 基础信息
- **基础URL**: `http://localhost:8081`
- **后台管理API前缀**: `/api/admin/questions`
- **前台展示API前缀**: `/api/public/questions`
- **数据格式**: JSON
- **字符编码**: UTF-8

## 接口分类说明

### 后台管理接口 (`/api/admin/questions`)
- 用于管理员对题目进行增删改查等管理操作
- 包含所有CRUD操作和管理功能
- 需要管理员权限（后续可配置权限控制）

### 前台展示接口 (`/api/public/questions`)
- 用于前台用户查看和获取题目信息
- 只包含查询操作，不包含增删改
- 只返回激活状态的题目
- 面向普通用户开放

## 通用响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1640995200000
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "错误信息",
  "data": null,
  "timestamp": 1640995200000
}
```

## API 接口列表

## 后台管理接口

### 1. 创建题目
- **URL**: `POST /api/admin/questions`
- **描述**: 创建新题目（管理员）
- **请求体**:
```json
{
  "title": "题目标题",
  "content": "题目内容",
  "type": "SINGLE_CHOICE",
  "difficulty": "MEDIUM",
  "category": "分类名称",
  "score": 2,
  "explanation": "答案解析",
  "status": "ACTIVE",
  "options": [
    {
      "optionLabel": "A",
      "optionContent": "选项内容",
      "isCorrect": true,
      "sortOrder": 1
    }
  ]
}
```

### 2. 更新题目
- **URL**: `PUT /api/admin/questions/{id}`
- **描述**: 更新指定题目（管理员）更新指定ID的题目
- **路径参数**: `id` - 题目ID
- **请求体**: 同创建题目

### 3. 获取题目详情
- **URL**: `GET /api/admin/questions/{id}`
- **描述**: 根据ID获取题目详情（管理员，包含所有状态）
- **路径参数**: `id` - 题目ID

### 4. 删除题目
- **URL**: `DELETE /api/admin/questions/{id}`
- **描述**: 删除指定题目（管理员）
- **路径参数**: `id` - 题目ID

### 5. 分页查询题目
- **URL**: `GET /api/admin/questions`
- **描述**: 分页查询题目，支持多条件筛选（管理员，包含所有状态）
- **查询参数**:
  - `type` - 题目类型 (SINGLE_CHOICE, MULTIPLE_CHOICE)
  - `difficulty` - 难度 (EASY, MEDIUM, HARD)
  - `category` - 分类
  - `status` - 状态 (ACTIVE, INACTIVE)
  - `keyword` - 关键词搜索
  - `page` - 页码 (默认0)
  - `size` - 每页大小 (默认10)
  - `sortBy` - 排序字段 (默认createdTime)
  - `sortDirection` - 排序方向 (asc, desc，默认desc)

### 6. 按类型查询题目
- **URL**: `GET /api/admin/questions/type/{type}`
- **描述**: 根据题目类型查询（管理员）
- **路径参数**: `type` - 题目类型

### 7. 按分类查询题目
- **URL**: `GET /api/admin/questions/category/{category}`
- **描述**: 根据分类查询题目（管理员）
- **路径参数**: `category` - 分类名称

### 8. 按难度查询题目
- **URL**: `GET /api/admin/questions/difficulty/{difficulty}`
- **描述**: 根据难度查询题目（管理员）
- **路径参数**: `difficulty` - 难度等级

### 9. 按状态查询题目
- **URL**: `GET /api/admin/questions/status/{status}`
- **描述**: 根据状态查询题目（管理员）
- **路径参数**: `status` - 题目状态

### 10. 搜索题目
- **URL**: `GET /api/admin/questions/search`
- **描述**: 根据关键词搜索题目（管理员）
- **查询参数**: `keyword` - 搜索关键词

### 11. 获取统计信息
- **URL**: `GET /api/admin/questions/statistics`
- **描述**: 获取题目统计信息（管理员）
- **响应示例**:
```json
{
  "code": 200,
  "data": {
    "totalQuestions": 100,
    "typeStatistics": {
      "SINGLE_CHOICE": 60,
      "MULTIPLE_CHOICE": 40
    },
    "difficultyStatistics": {
      "EASY": 30,
      "MEDIUM": 50,
      "HARD": 20
    }
  }
}
```

### 12. 批量删除题目
- **URL**: `DELETE /api/admin/questions/batch`
- **描述**: 批量删除题目（管理员）
- **请求体**:
```json
[1, 2, 3, 4, 5]
```

### 13. 批量更新状态
- **URL**: `PUT /api/admin/questions/batch/status`
- **描述**: 批量更新题目状态（管理员）
- **请求体**:
```json
{
  "ids": [1, 2, 3],
  "status": "INACTIVE"
}
  ```

## 前台展示接口

### 1. 获取题目详情
- **URL**: `GET /api/public/questions/{id}`
- **描述**: 根据ID获取题目详情（仅返回激活状态的题目）
- **路径参数**: `id` - 题目ID

### 2. 分页查询激活题目
- **URL**: `GET /api/public/questions`
- **描述**: 分页查询激活状态的题目
- **查询参数**:
  - `type` - 题目类型 (SINGLE_CHOICE, MULTIPLE_CHOICE)
  - `difficulty` - 难度 (EASY, MEDIUM, HARD)
  - `category` - 分类
  - `keyword` - 搜索关键词
  - `page` - 页码 (默认: 0)
  - `size` - 每页大小 (默认: 10)
  - `sortBy` - 排序字段 (默认: createdTime)
  - `sortDirection` - 排序方向 (默认: desc)

### 3. 根据类型查询激活题目
- **URL**: `GET /api/public/questions/type/{type}`
- **描述**: 根据题目类型查询激活状态的题目
- **路径参数**: `type` - 题目类型

### 4. 根据分类查询激活题目
- **URL**: `GET /api/public/questions/category/{category}`
- **描述**: 根据分类查询激活状态的题目
- **路径参数**: `category` - 题目分类

### 5. 根据难度查询激活题目
- **URL**: `GET /api/public/questions/difficulty/{difficulty}`
- **描述**: 根据难度查询激活状态的题目
- **路径参数**: `difficulty` - 题目难度

### 6. 搜索激活题目
- **URL**: `GET /api/public/questions/search`
- **描述**: 根据关键词搜索激活状态的题目
- **查询参数**: `keyword` - 搜索关键词

### 7. 获取激活题目统计
- **URL**: `GET /api/public/questions/statistics`
- **描述**: 获取激活题目的统计信息

### 8. 随机获取题目
- **URL**: `GET /api/public/questions/random`
- **描述**: 随机获取指定数量的激活题目（用于考试）
- **查询参数**:
  - `count` - 题目数量 (默认: 10)
  - `type` - 题目类型 (可选)
  - `difficulty` - 难度 (可选)
  - `category` - 分类 (可选)

### 9. 随机获取考试题目（不含答案和解析）
- **URL**: `GET /api/public/questions/exam/random`
- **描述**: 随机获取一道考试题目，返回的题目不包含答案和解析信息，适用于考试场景
- **查询参数**:
  - `type` - 题目类型 (可选)
  - `difficulty` - 难度 (可选)
  - `category` - 分类 (可选)

### 10. 获取题目答案和解析
- **URL**: `GET /api/public/questions/{id}/answer`
- **描述**: 获取指定题目的答案和解析信息，只能获取激活状态题目的答案
- **路径参数**: `id` - 题目ID

## 数据字典

### 题目类型 (QuestionType)
- `SINGLE_CHOICE`: 单选题
- `MULTIPLE_CHOICE`: 多选题

### 难度等级 (Difficulty)
- `EASY`: 简单
- `MEDIUM`: 中等
- `HARD`: 困难

### 题目状态 (QuestionStatus)
- `ACTIVE`: 启用
- `INACTIVE`: 禁用

## 错误码说明
- `200`: 操作成功
- `400`: 请求参数错误
- `404`: 资源不存在
- `500`: 服务器内部错误

## 使用示例

### 后台管理 - 创建题目
```bash
curl -X POST http://localhost:8081/api/admin/questions \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Java中哪个关键字用于继承？",
    "content": "在Java中，哪个关键字用于实现类的继承？",
    "type": "SINGLE_CHOICE",
    "difficulty": "EASY",
    "category": "Java基础",
    "score": 2,
    "explanation": "extends关键字用于类的继承",
    "status": "ACTIVE",
    "options": [
      {"optionLabel": "A", "optionContent": "extends", "isCorrect": true, "sortOrder": 1},
      {"optionLabel": "B", "optionContent": "implements", "isCorrect": false, "sortOrder": 2},
      {"optionLabel": "C", "optionContent": "inherit", "isCorrect": false, "sortOrder": 3},
      {"optionLabel": "D", "optionContent": "super", "isCorrect": false, "sortOrder": 4}
    ]
  }'
```

### 后台管理 - 分页查询题目
```bash
curl "http://localhost:8081/api/admin/questions?page=0&size=5&type=SINGLE_CHOICE&difficulty=EASY"
```

### 前台展示 - 获取激活题目
```bash
curl "http://localhost:8081/api/public/questions?page=0&size=10&type=SINGLE_CHOICE"
```

### 前台展示 - 随机获取题目
```bash
curl "http://localhost:8081/api/public/questions/random?count=5&difficulty=MEDIUM"
```

### 前台展示 - 随机获取考试题目（不含答案）
```bash
curl "http://localhost:8081/api/public/questions/exam/random?type=SINGLE_CHOICE&difficulty=EASY"
```

### 前台展示 - 获取题目答案和解析
```bash
curl "http://localhost:8081/api/public/questions/1/answer"
```