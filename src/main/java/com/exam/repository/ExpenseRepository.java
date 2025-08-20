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
    
    /**
     * 获取日期范围内的总金额
     */
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.expenseDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalAmountByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * 获取日期范围内的记录总数
     */
    @Query("SELECT COUNT(e) FROM Expense e WHERE e.expenseDate BETWEEN :startDate AND :endDate")
    Long getTotalCountByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * 获取日期范围内按分类统计的数据
     */
    @Query(value = "SELECT e.category_name as categoryName, " +
           "SUM(e.amount) as totalAmount, " +
           "COUNT(e.id) as totalCount " +
           "FROM expenses e " +
           "WHERE e.expense_date BETWEEN :startDate AND :endDate " +
           "GROUP BY e.category_name " +
           "ORDER BY SUM(e.amount) DESC", nativeQuery = true)
    List<Object[]> getCategoryStatisticsByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * 获取日期范围内按月统计的数据
     */
    @Query(value = "SELECT CONCAT(YEAR(e.expense_date), '-', LPAD(MONTH(e.expense_date), 2, '0')) as month, " +
           "SUM(e.amount) as totalAmount, " +
           "COUNT(e.id) as totalCount " +
           "FROM expenses e " +
           "WHERE e.expense_date BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(e.expense_date), MONTH(e.expense_date) " +
           "ORDER BY YEAR(e.expense_date), MONTH(e.expense_date)", nativeQuery = true)
    List<Object[]> getMonthlyStatisticsByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * 获取日期范围内按日统计的数据
     */
    @Query(value = "SELECT DATE(e.expense_date) as date, " +
           "SUM(e.amount) as totalAmount, " +
           "COUNT(e.id) as totalCount " +
           "FROM expenses e " +
           "WHERE e.expense_date BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(e.expense_date) " +
           "ORDER BY DATE(e.expense_date)", nativeQuery = true)
    List<Object[]> getDailyStatisticsByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}