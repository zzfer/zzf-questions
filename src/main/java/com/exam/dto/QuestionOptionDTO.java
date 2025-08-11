package com.exam.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 题目选项数据传输对象
 */
@Data
public class QuestionOptionDTO {
    
    private Long id;
    
    private String optionLabel;
    
    private String optionContent;
    
    private Boolean isCorrect;
    
    private Integer sortOrder;
    
    private LocalDateTime createdTime;
}