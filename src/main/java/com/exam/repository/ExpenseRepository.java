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
     * 根据用户ID查询支出记录
     */
    List<Expense> findByUserIdOrderByExpenseDateDesc(Long userId);
    
    /**
     * 根据用户ID和日期范围查询支出记录
     */
    List<Expense> findByUserIdAndExpenseDateBetweenOrderByExpenseDateDesc(Long userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 计算用户在指定日期范围内的支出总额
     */
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.userId = :userId AND e.expenseDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalExpenseByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * 计算用户总支出
     */
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.userId = :userId")
    BigDecimal calculateTotalExpenseByUserId(@Param("userId") Long userId);
    
    /**
     * 计算所有人在指定日期范围内的支出总额
     */
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.expenseDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalExpenseByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * 计算所有人的总支出
     */
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e")
    BigDecimal calculateTotalExpense();
    
    /**
     * 根据支出类型计算用户支出
     */
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.userId = :userId AND e.categoryName = :category")
    BigDecimal calculateExpenseByUserIdAndCategory(@Param("userId") Long userId, @Param("category") String category);
    
    /**
     * 删除用户的支出记录
     */
    void deleteByUserId(Long userId);
    
    /**
     * 统计用户的支出记录数量
     */
    long countByUserId(Long userId);
    
    /**
     * 获取用户最近的支出记录
     */
    List<Expense> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
    
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
    
    /**
     * 根据多条件查询支出记录
     */
    @Query("SELECT e FROM Expense e WHERE e.expenseDate BETWEEN :startDate AND :endDate " +
           "AND (:categoryName IS NULL OR e.categoryName = :categoryName) " +
           "AND (:payer IS NULL OR e.payer = :payer) " +
           "AND (:isPublicExpense IS NULL OR e.isPublicExpense = :isPublicExpense) " +
           "ORDER BY e.expenseDate DESC")
    List<Expense> findByMultipleConditions(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("categoryName") String categoryName,
        @Param("payer") String payer,
        @Param("isPublicExpense") Boolean isPublicExpense
    );
    
    /**
     * 根据多条件计算支出总额
     */
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.expenseDate BETWEEN :startDate AND :endDate " +
           "AND (:categoryName IS NULL OR e.categoryName = :categoryName) " +
           "AND (:payer IS NULL OR e.payer = :payer) " +
           "AND (:isPublicExpense IS NULL OR e.isPublicExpense = :isPublicExpense)")
    BigDecimal calculateTotalAmountByMultipleConditions(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("categoryName") String categoryName,
        @Param("payer") String payer,
        @Param("isPublicExpense") Boolean isPublicExpense
    );
    
    /**
     * 根据多条件计算支出记录数
     */
    @Query("SELECT COUNT(e) FROM Expense e WHERE e.expenseDate BETWEEN :startDate AND :endDate " +
           "AND (:categoryName IS NULL OR e.categoryName = :categoryName) " +
           "AND (:payer IS NULL OR e.payer = :payer) " +
           "AND (:isPublicExpense IS NULL OR e.isPublicExpense = :isPublicExpense)")
    Long countByMultipleConditions(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("categoryName") String categoryName,
        @Param("payer") String payer,
        @Param("isPublicExpense") Boolean isPublicExpense
    );
    
    /**
     * 计算用户在指定日期范围内实际有支出记录的天数
     */
    @Query("SELECT COUNT(DISTINCT e.expenseDate) FROM Expense e WHERE e.userId = :userId AND e.expenseDate BETWEEN :startDate AND :endDate")
    Long countDistinctExpenseDaysByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * 计算所有人在指定日期范围内实际有支出记录的天数
     */
    @Query("SELECT COUNT(DISTINCT e.expenseDate) FROM Expense e WHERE e.expenseDate BETWEEN :startDate AND :endDate")
    Long countDistinctExpenseDaysByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}