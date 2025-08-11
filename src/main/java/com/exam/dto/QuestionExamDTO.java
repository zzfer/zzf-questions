package com.exam.dto;

import com.exam.enums.Difficulty;
import com.exam.enums.QuestionType;
import lombok.Data;

import java.util.List;

/**
 * 考试用题目数据传输对象（不包含答案和解析）
 */
@Data
public class QuestionExamDTO {
    
    private Long id;
    
    private String title;
    
    private String content;
    
    private QuestionType type;
    
    private Difficulty difficulty;
    
    private String category;
    
    private Integer score;
    
    private List<QuestionOptionExamDTO> options;
}