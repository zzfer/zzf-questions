package com.exam.dto;

import com.exam.enums.Difficulty;
import com.exam.enums.QuestionStatus;
import com.exam.enums.QuestionType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 题目数据传输对象
 */
@Data
public class QuestionDTO {
    
    private Long id;
    
    private String title;
    
    private String content;
    
    private QuestionType type;
    
    private Difficulty difficulty;
    
    private String category;
    
    private Integer score;
    
    private String explanation;
    
    private QuestionStatus status;
    
    private LocalDateTime createdTime;
    
    private LocalDateTime updatedTime;
    
    private List<QuestionOptionDTO> options;
}