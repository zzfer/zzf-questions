package com.exam.service;

import com.exam.common.PageResult;
import com.exam.dto.QuestionDTO;
import com.exam.dto.QuestionQueryDTO;
import com.exam.dto.QuestionExamDTO;
import com.exam.dto.QuestionAnswerDTO;
import com.exam.enums.Difficulty;
import com.exam.enums.QuestionStatus;
import com.exam.enums.QuestionType;

import java.util.List;
import java.util.Map;

/**
 * 题目服务接口
 */
public interface QuestionService {
    
    /**
     * 创建题目
     */
    QuestionDTO createQuestion(QuestionDTO questionDTO);
    
    /**
     * 更新题目
     */
    QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO);
    
    /**
     * 根据ID查询题目
     */
    QuestionDTO getQuestionById(Long id);
    
    /**
     * 删除题目
     */
    void deleteQuestion(Long id);
    
    /**
     * 分页查询题目
     */
    PageResult<QuestionDTO> getQuestions(QuestionQueryDTO queryDTO);
    
    /**
     * 根据类型查询题目
     */
    List<QuestionDTO> getQuestionsByType(QuestionType type);
    
    /**
     * 根据分类查询题目
     */
    List<QuestionDTO> getQuestionsByCategory(String category);
    
    /**
     * 根据难度查询题目
     */
    List<QuestionDTO> getQuestionsByDifficulty(Difficulty difficulty);
    
    /**
     * 根据状态查询题目
     */
    List<QuestionDTO> getQuestionsByStatus(QuestionStatus status);
    
    /**
     * 模糊查询题目
     */
    List<QuestionDTO> searchQuestions(String keyword);
    
    /**
     * 获取题目统计信息
     */
    Map<String, Object> getQuestionStatistics();
    
    /**
     * 批量删除题目
     */
    void deleteQuestions(List<Long> ids);
    
    /**
     * 批量更新题目状态
     */
    void updateQuestionStatus(List<Long> ids, QuestionStatus status);
    
    /**
     * 随机获取一道考试题目（不包含答案和解析）
     */
    QuestionExamDTO getRandomExamQuestion(QuestionType type, Difficulty difficulty, String category);
    
    /**
     * 获取题目的答案和解析
     */
    QuestionAnswerDTO getQuestionAnswer(Long questionId);
}