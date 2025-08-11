package com.exam.enums;

/**
 * 题目状态枚举
 */
public enum QuestionStatus {
    ACTIVE("启用"),
    INACTIVE("禁用");

    private final String description;

    QuestionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}