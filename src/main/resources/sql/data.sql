-- 可选的初始数据脚本
-- 如果需要在数据库初始化时插入特定数据，可以在这里添加
-- 要启用此脚本，请在application.properties中取消注释:
-- spring.sql.init.data-locations=classpath:sql/data.sql

-- 示例：插入默认的支出类别数据
-- INSERT INTO expense_categories (name, description) VALUES 
-- ('餐饮', '日常用餐支出'),
-- ('交通', '出行交通费用'),
-- ('购物', '日用品购买'),
-- ('娱乐', '娱乐休闲支出'),
-- ('医疗', '医疗健康支出'),
-- ('教育', '学习教育支出'),
-- ('其他', '其他类型支出');

-- 示例：插入默认用户数据
-- INSERT INTO users (username, email, role) VALUES 
-- ('admin', 'admin@example.com', 'ADMIN'),
-- ('user', 'user@example.com', 'USER');

-- 注意：实际使用时请根据具体的表结构和需求修改上述SQL语句