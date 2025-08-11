package com.exam.dto;

import lombok.Data;

/**
 * 考试用题目选项数据传输对象（不包含正确答案标识）
 */
@Data
public class QuestionOptionExamDTO {
    
    private Long id;
    
    private String optionKey;
    
    private String optionValue;
}