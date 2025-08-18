package com.exam.repository;

import com.exam.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 记账数据访问层
 */
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    /**
     * 根据日期范围查询记账记录
     */
    List<Expense> findByExpenseDateBetweenOrderByExpenseDateDesc(LocalDate startDate, LocalDate endDate);
    
    /**
     * 根据分类查询记账记录
     */
    List<Expense> findByCategoryOrderByExpenseDateDesc(String category);
    
    /**
     * 根据分类名称查询记账记录
     */
    List<Expense> findByCategoryNameOrderByExpenseDateDesc(String categoryName);
    
    /**
     * 根据日期范围和分类查询记账记录
     */
    List<Expense> findByExpenseDateBetweenAndCategoryOrderByExpenseDateDesc(
            LocalDate startDate, LocalDate endDate, String category);
    
    /**
     * 根据日期范围和分类名称查询记账记录
     */
    List<Expense> findByExpenseDateBetweenAndCategoryNameOrderByExpenseDateDesc(
            LocalDate startDate, LocalDate endDate, String categoryName);
    
    /**
     * 获取最近的记账记录
     */
    List<Expense> findTop10ByOrderByCreatedAtDesc();
}