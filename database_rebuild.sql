-- =============================================
-- 记账系统表结构修复脚本
-- 创建时间: 2025-08-18
-- 说明: 只修改当前业务相关的表，不影响数据库中的其他表
-- =============================================

USE `questions`;

-- =============================================
-- 1. 备份现有数据（如果表存在）
-- =============================================

-- 备份现有支出数据
DROP TABLE IF EXISTS `expenses_backup`;
CREATE TABLE `expenses_backup` AS SELECT * FROM `expenses` WHERE 1=1;

-- 备份现有分类数据  
DROP TABLE IF EXISTS `categories_backup`;
CREATE TABLE `categories_backup` AS SELECT * FROM `categories` WHERE 1=1;

-- =============================================
-- 2. 删除现有表（按依赖关系顺序）
-- =============================================

-- 删除支出表（有外键依赖）
DROP TABLE IF EXISTS `expenses`;

-- 删除分类表
DROP TABLE IF EXISTS `categories`;

-- =============================================
-- 3. 重新创建分类表 (categories)
-- =============================================
CREATE TABLE `categories` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID，主键',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `icon` VARCHAR(100) DEFAULT NULL COMMENT '分类图标名称（Element Plus图标）',
    `color` VARCHAR(20) DEFAULT '#409EFF' COMMENT '分类颜色（十六进制颜色值）',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '分类描述',
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否启用（TRUE=启用，FALSE=禁用）',
    `sort_order` INT DEFAULT 0 COMMENT '排序顺序（数字越小越靠前）',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_categories_name` (`name`),
    KEY `idx_categories_active_sort` (`is_active`, `sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支出分类表';

-- =============================================
-- 4. 重新创建支出记录表 (expenses)
-- =============================================
CREATE TABLE `expenses` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '支出记录ID，主键',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '支出金额（精确到分）',
    `category_id` BIGINT NOT NULL COMMENT '分类ID，外键关联categories表',
    `category_name` VARCHAR(50) DEFAULT NULL COMMENT '分类名称（冗余字段，用于查询优化）',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '支出备注说明',
    `expense_date` DATE NOT NULL COMMENT '支出日期',
    `payer` VARCHAR(20) NOT NULL COMMENT '支出人姓名',
    `is_public_expense` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否公共支出（TRUE=公共，FALSE=个人）',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户ID（预留字段，用于多用户支持）',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_expenses_category` (`category_id`),
    KEY `idx_expenses_date` (`expense_date`),
    KEY `idx_expenses_payer` (`payer`),
    KEY `idx_expenses_public` (`is_public_expense`),
    KEY `idx_expenses_user` (`user_id`),
    KEY `idx_expenses_date_category` (`expense_date`, `category_id`),
    KEY `idx_expenses_payer_date` (`payer`, `expense_date`),
    KEY `idx_expenses_public_date` (`is_public_expense`, `expense_date`),
    CONSTRAINT `fk_expenses_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支出记录表';

-- =============================================
-- 5. 插入分类数据
-- =============================================

-- 先尝试从备份恢复分类数据
INSERT IGNORE INTO `categories` (`id`, `name`, `icon`, `color`, `description`, `is_active`, `sort_order`, `created_at`, `updated_at`)
SELECT `id`, `name`, `icon`, `color`, `description`, `is_active`, `sort_order`, `created_at`, `updated_at`
FROM `categories_backup` 
WHERE EXISTS (SELECT 1 FROM `categories_backup`);

-- 如果没有备份数据，插入默认分类
INSERT IGNORE INTO `categories` (`name`, `icon`, `color`, `description`, `sort_order`) VALUES
('餐饮', 'Dish', '#FF6B6B', '日常用餐、外卖、聚餐等餐饮支出', 1),
('交通', 'Car', '#4ECDC4', '公交、地铁、打车、加油等交通费用', 2),
('购物', 'ShoppingBag', '#45B7D1', '日用品、服装、电子产品等购物支出', 3),
('娱乐', 'VideoPlay', '#96CEB4', '电影、游戏、KTV等娱乐活动支出', 4),
('医疗', 'FirstAidKit', '#FFEAA7', '看病、买药、体检等医疗健康支出', 5),
('教育', 'Reading', '#DDA0DD', '学费、培训、书籍等教育学习支出', 6),
('住房', 'House', '#98D8C8', '房租、水电费、物业费等住房支出', 7),
('通讯', 'Phone', '#F7DC6F', '话费、网费、流量等通讯费用', 8),
('其他', 'More', '#BDC3C7', '其他未分类的支出', 99);

-- =============================================
-- 6. 恢复支出数据（如果有备份且结构兼容）
-- =============================================

-- 尝试恢复支出数据，只恢复结构兼容的字段
INSERT IGNORE INTO `expenses` (
    `id`, `amount`, `category_id`, `category_name`, `description`, 
    `expense_date`, `payer`, `is_public_expense`, `user_id`, `created_at`, `updated_at`
)
SELECT 
    eb.`id`,
    eb.`amount`,
    COALESCE(c.`id`, 1) as `category_id`,  -- 如果找不到对应分类，默认使用ID=1的分类
    c.`name` as `category_name`,
    eb.`description`,
    eb.`expense_date`,
    eb.`payer`,
    COALESCE(eb.`is_public_expense`, FALSE) as `is_public_expense`,
    eb.`user_id`,
    eb.`created_at`,
    eb.`updated_at`
FROM `expenses_backup` eb
LEFT JOIN `categories` c ON c.`name` = eb.`category_name` OR c.`id` = eb.`category_id`
WHERE EXISTS (SELECT 1 FROM `expenses_backup`);

-- 如果没有备份数据，插入一些示例数据
INSERT IGNORE INTO `expenses` (`amount`, `category_id`, `category_name`, `description`, `expense_date`, `payer`, `is_public_expense`) 
SELECT 50.00, c.`id`, c.`name`, '午餐 - 公司附近餐厅', CURDATE() - INTERVAL 1 DAY, '张三', FALSE
FROM `categories` c WHERE c.`name` = '餐饮' AND NOT EXISTS (SELECT 1 FROM `expenses_backup`);

INSERT IGNORE INTO `expenses` (`amount`, `category_id`, `category_name`, `description`, `expense_date`, `payer`, `is_public_expense`) 
SELECT 120.00, c.`id`, c.`name`, '打车费用 - 去机场', CURDATE(), '李四', TRUE
FROM `categories` c WHERE c.`name` = '交通' AND NOT EXISTS (SELECT 1 FROM `expenses_backup`);

INSERT IGNORE INTO `expenses` (`amount`, `category_id`, `category_name`, `description`, `expense_date`, `payer`, `is_public_expense`) 
SELECT 200.00, c.`id`, c.`name`, '日用品采购 - 超市购物', CURDATE() - INTERVAL 2 DAY, '王五', FALSE
FROM `categories` c WHERE c.`name` = '购物' AND NOT EXISTS (SELECT 1 FROM `expenses_backup`);

-- =============================================
-- 7. 创建触发器 - 自动更新category_name
-- =============================================
DROP TRIGGER IF EXISTS `tr_expenses_update_category_name`;

DELIMITER //
CREATE TRIGGER `tr_expenses_update_category_name`
BEFORE INSERT ON `expenses`
FOR EACH ROW
BEGIN
    DECLARE `cat_name` VARCHAR(50);
    
    -- 获取分类名称
    SELECT `name` INTO `cat_name` 
    FROM `categories` 
    WHERE `id` = NEW.`category_id` AND `is_active` = TRUE;
    
    -- 设置分类名称
    IF `cat_name` IS NOT NULL THEN
        SET NEW.`category_name` = `cat_name`;
    END IF;
END //
DELIMITER ;

-- =============================================
-- 8. 创建或更新统计视图
-- =============================================
DROP VIEW IF EXISTS `v_expense_statistics`;

CREATE VIEW `v_expense_statistics` AS
SELECT 
    c.`name` AS `category_name`,
    c.`icon` AS `category_icon`,
    c.`color` AS `category_color`,
    COUNT(e.`id`) AS `expense_count`,
    COALESCE(SUM(e.`amount`), 0) AS `total_amount`,
    COALESCE(AVG(e.`amount`), 0) AS `avg_amount`,
    MAX(e.`expense_date`) AS `last_expense_date`
FROM `categories` c
LEFT JOIN `expenses` e ON c.`id` = e.`category_id`
WHERE c.`is_active` = TRUE
GROUP BY c.`id`, c.`name`, c.`icon`, c.`color`
ORDER BY `total_amount` DESC;

-- =============================================
-- 9. 清理备份表（可选，如果确认数据正确可以删除备份）
-- =============================================
-- DROP TABLE IF EXISTS `expenses_backup`;
-- DROP TABLE IF EXISTS `categories_backup`;

-- =============================================
-- 10. 验证数据
-- =============================================
SELECT 'Table structure update completed successfully!' AS message;
SELECT COUNT(*) AS categories_count FROM categories;
SELECT COUNT(*) AS expenses_count FROM expenses;

-- 显示分类信息
SELECT `id`, `name`, `icon`, `color` FROM `categories` ORDER BY `sort_order`;

-- 显示最近的支出记录
SELECT `id`, `amount`, `category_name`, `description`, `expense_date`, `payer` 
FROM `expenses` 
ORDER BY `created_at` DESC 
LIMIT 5;