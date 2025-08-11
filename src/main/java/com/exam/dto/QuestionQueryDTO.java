package com.exam.dto;

import com.exam.enums.Difficulty;
import com.exam.enums.QuestionStatus;
import com.exam.enums.QuestionType;
import lombok.Data;

/**
 * 题目查询请求DTO
 */
@Data
public class QuestionQueryDTO {
    
    private QuestionType type;
    
    private Difficulty difficulty;
    
    private String category;
    
    private QuestionStatus status;
    
    private String keyword;
    
    private Integer page = 0;
    
    private Integer size = 10;
    
    private String sortBy = "createdTime";
    
    private String sortDirection = "desc";
}