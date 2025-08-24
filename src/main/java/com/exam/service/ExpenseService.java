package com.exam.service;

import com.exam.dto.ExpenseDTO;
import com.exam.dto.ExpenseStatisticsDTO;
import com.exam.enums.ExpenseCategory;

import java.time.LocalDate;
import java.util.List;

/**
 * 记账服务接口
 */
public interface ExpenseService {
    
    /**
     * 创建记账记录
     */
    ExpenseDTO createExpense(ExpenseDTO expenseDTO);
    
    /**
     * 更新记账记录
     */
    ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO);
    
    /**
     * 删除记账记录
     */
    void deleteExpense(Long id);
    
    /**
     * 根据ID查询记账记录
     */
    ExpenseDTO getExpenseById(Long id);
    
    /**
     * 查询所有记账记录
     */
    List<ExpenseDTO> getAllExpenses();
    
    /**
     * 根据日期范围查询记账记录
     */
    List<ExpenseDTO> getExpensesByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * 根据分类查询记账记录
     */
    List<ExpenseDTO> getExpensesByCategory(String category);
    
    /**
     * 获取最近的记账记录
     */
    List<ExpenseDTO> getRecentExpenses();
    
    /**
     * 获取支出统计数据
     */
    ExpenseStatisticsDTO getStatistics(LocalDate startDate, LocalDate endDate);
    
    /**
     * 根据多条件获取支出统计数据
     */
    ExpenseStatisticsDTO getStatistics(LocalDate startDate, LocalDate endDate, String categoryName, String payer, Boolean isPublicExpense);
    
    /**
     * 根据多条件查询支出记录
     */
    List<ExpenseDTO> getExpensesByFilters(LocalDate startDate, LocalDate endDate, String categoryName, String payer, Boolean isPublicExpense);
    
    /**
     * 获取所有可用的分类
     */
    List<ExpenseCategory> getAllCategories();
}