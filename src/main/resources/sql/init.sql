-- 创建数据库
CREATE DATABASE IF NOT EXISTS zzf_questions DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE zzf_questions;

-- 题目表
CREATE TABLE questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '题目ID',
    title VARCHAR(500) NOT NULL COMMENT '题目标题',
    content TEXT COMMENT '题目详细内容/描述',
    type ENUM('SINGLE_CHOICE', 'MULTIPLE_CHOICE') NOT NULL COMMENT '题目类型：单选、多选',
    difficulty ENUM('EASY', 'MEDIUM', 'HARD') DEFAULT 'MEDIUM' COMMENT '难度等级',
    category VARCHAR(100) COMMENT '题目分类',
    score INT DEFAULT 1 COMMENT '题目分值',
    explanation TEXT COMMENT '答案解析',
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE' COMMENT '题目状态',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='题目表';

-- 选项表
CREATE TABLE question_options (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '选项ID',
    question_id BIGINT NOT NULL COMMENT '关联题目ID',
    option_label CHAR(1) NOT NULL COMMENT '选项标签 A,B,C,D',
    option_content TEXT NOT NULL COMMENT '选项内容',
    is_correct BOOLEAN DEFAULT FALSE COMMENT '是否为正确答案',
    sort_order INT DEFAULT 0 COMMENT '选项排序',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
    INDEX idx_question_id (question_id),
    INDEX idx_question_sort (question_id, sort_order)
) COMMENT='题目选项表';

-- 创建索引
CREATE INDEX idx_questions_type ON questions(type);
CREATE INDEX idx_questions_category ON questions(category);
CREATE INDEX idx_questions_status ON questions(status);
CREATE INDEX idx_questions_difficulty ON questions(difficulty);
CREATE INDEX idx_questions_created_time ON questions(created_time);