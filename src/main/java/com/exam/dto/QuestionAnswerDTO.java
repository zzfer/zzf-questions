package com.exam.dto;

import lombok.Data;

import java.util.List;

/**
 * 题目答案解析数据传输对象
 */
@Data
public class QuestionAnswerDTO {
    
    private Long questionId;
    
    private String explanation;
    
    private List<QuestionOptionDTO> correctOptions;
}