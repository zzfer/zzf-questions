package com.exam.repository;

import com.exam.entity.Question;
import com.exam.enums.Difficulty;
import com.exam.enums.QuestionStatus;
import com.exam.enums.QuestionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 题目Repository接口
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    /**
     * 根据状态查询题目
     */
    List<Question> findByStatus(QuestionStatus status);
    
    /**
     * 根据类型查询题目
     */
    List<Question> findByType(QuestionType type);
    
    /**
     * 根据分类查询题目
     */
    List<Question> findByCategory(String category);
    
    /**
     * 根据难度查询题目
     */
    List<Question> findByDifficulty(Difficulty difficulty);
    
    /**
     * 分页查询题目（支持多条件筛选）
     */
    @Query("SELECT q FROM Question q WHERE " +
           "(:type IS NULL OR q.type = :type) AND " +
           "(:difficulty IS NULL OR q.difficulty = :difficulty) AND " +
           "(:category IS NULL OR q.category = :category) AND " +
           "(:status IS NULL OR q.status = :status) AND " +
           "(:keyword IS NULL OR q.title LIKE %:keyword% OR q.content LIKE %:keyword%)")
    Page<Question> findQuestionsWithFilters(
            @Param("type") QuestionType type,
            @Param("difficulty") Difficulty difficulty,
            @Param("category") String category,
            @Param("status") QuestionStatus status,
            @Param("keyword") String keyword,
            Pageable pageable);
    
    /**
     * 根据题目标题模糊查询
     */
    List<Question> findByTitleContaining(String title);
    
    /**
     * 统计各类型题目数量
     */
    @Query("SELECT q.type, COUNT(q) FROM Question q WHERE q.status = :status GROUP BY q.type")
    List<Object[]> countByTypeAndStatus(@Param("status") QuestionStatus status);
    
    /**
     * 统计各难度题目数量
     */
    @Query("SELECT q.difficulty, COUNT(q) FROM Question q WHERE q.status = :status GROUP BY q.difficulty")
    List<Object[]> countByDifficultyAndStatus(@Param("status") QuestionStatus status);
}