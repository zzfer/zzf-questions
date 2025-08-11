package com.exam.repository;

import com.exam.entity.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 题目选项Repository接口
 */
@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {
    
    /**
     * 根据题目ID查询选项
     */
    List<QuestionOption> findByQuestionIdOrderBySortOrder(Long questionId);
    
    /**
     * 根据题目ID查询正确答案
     */
    @Query("SELECT o FROM QuestionOption o WHERE o.question.id = :questionId AND o.isCorrect = true ORDER BY o.sortOrder")
    List<QuestionOption> findCorrectOptionsByQuestionId(@Param("questionId") Long questionId);
    
    /**
     * 根据题目ID删除选项
     */
    void deleteByQuestionId(Long questionId);
    
    /**
     * 统计题目的选项数量
     */
    long countByQuestionId(Long questionId);
}