package com.exam.service.impl;

import com.exam.dto.ExpenseDTO;
import com.exam.dto.ExpenseStatisticsDTO;
import com.exam.entity.Expense;
import com.zzf.entity.Category;
import com.exam.enums.ExpenseCategory;
import com.exam.repository.ExpenseRepository;
import com.zzf.repository.CategoryRepository;
import com.exam.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 记账服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ExpenseServiceImpl implements ExpenseService {
    
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    
    @Override
    public ExpenseDTO createExpense(ExpenseDTO expenseDTO) {
        log.info("创建记账记录: {}", expenseDTO);
        
        // 查找分类
        Category category = categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("分类不存在，ID: " + expenseDTO.getCategoryId()));
        
        Expense expense = new Expense();
        expense.setAmount(expenseDTO.getAmount());
        expense.setCategory(category);
        expense.setCategoryName(category.getName());
        expense.setDescription(expenseDTO.getDescription());
        expense.setExpenseDate(expenseDTO.getExpenseDate());
        expense.setPayer(expenseDTO.getPayer());
        expense.setIsPublicExpense(expenseDTO.getIsPublicExpense());
        expense.setUserId(expenseDTO.getUserId());
        
        Expense savedExpense = expenseRepository.save(expense);
        
        log.info("记账记录创建成功，ID: {}", savedExpense.getId());
        return convertToDTO(savedExpense);
    }
    
    @Override
    public ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO) {
        log.info("更新记账记录，ID: {}, 数据: {}", id, expenseDTO);
        
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("记账记录不存在，ID: " + id));
        
        // 查找分类
        Category category = categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("分类不存在，ID: " + expenseDTO.getCategoryId()));
        
        // 更新字段
        expense.setAmount(expenseDTO.getAmount());
        expense.setCategory(category);
        expense.setCategoryName(category.getName());
        expense.setDescription(expenseDTO.getDescription());
        expense.setExpenseDate(expenseDTO.getExpenseDate());
        expense.setPayer(expenseDTO.getPayer());
        expense.setIsPublicExpense(expenseDTO.getIsPublicExpense());
        expense.setUserId(expenseDTO.getUserId());
        
        Expense savedExpense = expenseRepository.save(expense);
        
        log.info("记账记录更新成功，ID: {}", id);
        return convertToDTO(savedExpense);
    }
    
    @Override
    public void deleteExpense(Long id) {
        log.info("删除记账记录，ID: {}", id);
        
        if (!expenseRepository.existsById(id)) {
            throw new RuntimeException("记账记录不存在，ID: " + id);
        }
        
        expenseRepository.deleteById(id);
        log.info("记账记录删除成功，ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ExpenseDTO getExpenseById(Long id) {
        log.info("查询记账记录，ID: {}", id);
        
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("记账记录不存在，ID: " + id));
        
        return convertToDTO(expense);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ExpenseDTO> getAllExpenses() {
        log.info("查询所有记账记录");
        
        List<Expense> expenses = expenseRepository.findAll();
        
        return expenses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ExpenseDTO> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("查询日期范围内的记账记录: {} - {}", startDate, endDate);
        
        List<Expense> expenses = expenseRepository.findByExpenseDateBetweenOrderByExpenseDateDesc(startDate, endDate);
        
        return expenses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ExpenseDTO> getExpensesByCategory(String category) {
        log.info("查询分类为 {} 的记账记录", category);
        
        List<Expense> expenses = expenseRepository.findByCategoryNameOrderByExpenseDateDesc(category);
        
        return expenses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ExpenseDTO> getRecentExpenses() {
        log.info("查询最近的记账记录");
        
        List<Expense> expenses = expenseRepository.findTop10ByOrderByCreatedAtDesc();
        
        return expenses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public ExpenseStatisticsDTO getStatistics(LocalDate startDate, LocalDate endDate) {
        log.info("获取统计数据: {} - {}", startDate, endDate);
        
        try {
            List<Expense> expenses = expenseRepository.findByExpenseDateBetweenOrderByExpenseDateDesc(startDate, endDate);
            
            // 计算总支出
            BigDecimal totalAmount = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // 计算总记录数
            long totalCount = expenses.size();
            
            // 按分类统计
            Map<String, List<Expense>> categoryGroups = expenses.stream()
                .collect(Collectors.groupingBy(Expense::getCategoryName));
            
            List<ExpenseStatisticsDTO.CategoryStatistic> categoryStatistics = categoryGroups.entrySet().stream()
                .map(entry -> {
                    String categoryName = entry.getKey();
                    List<Expense> categoryExpenses = entry.getValue();
                    BigDecimal categoryTotal = categoryExpenses.stream()
                        .map(Expense::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    Long count = (long) categoryExpenses.size();
                    Double percentage = totalAmount.compareTo(BigDecimal.ZERO) > 0 ? 
                            categoryTotal.divide(totalAmount, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue() : 0.0;
                    return new ExpenseStatisticsDTO.CategoryStatistic(categoryName, categoryTotal, count, percentage);
                })
                .sorted((a, b) -> b.getAmount().compareTo(a.getAmount())) // 按金额降序排序
                .collect(Collectors.toList());
            
            // 按月统计
            Map<String, List<Expense>> monthlyGroups = expenses.stream()
                .collect(Collectors.groupingBy(expense -> 
                    expense.getExpenseDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"))));
            
            List<ExpenseStatisticsDTO.MonthlyStatistic> monthlyStatistics = monthlyGroups.entrySet().stream()
                .map(entry -> {
                    String month = entry.getKey();
                    List<Expense> monthExpenses = entry.getValue();
                    BigDecimal monthTotal = monthExpenses.stream()
                        .map(Expense::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    Long count = (long) monthExpenses.size();
                    return new ExpenseStatisticsDTO.MonthlyStatistic(month, monthTotal, count);
                })
                .collect(Collectors.toList());
            
            // 按日统计
            Map<LocalDate, List<Expense>> dailyGroups = expenses.stream()
                .collect(Collectors.groupingBy(Expense::getExpenseDate));
            
            List<ExpenseStatisticsDTO.DailyStatistic> dailyStatistics = dailyGroups.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<Expense> dayExpenses = entry.getValue();
                    BigDecimal dayTotal = dayExpenses.stream()
                        .map(Expense::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    Long count = (long) dayExpenses.size();
                    return new ExpenseStatisticsDTO.DailyStatistic(date, dayTotal, count);
                })
                .collect(Collectors.toList());
            
            ExpenseStatisticsDTO statistics = new ExpenseStatisticsDTO();
            statistics.setTotalAmount(totalAmount);
            statistics.setTotalCount(totalCount);
            statistics.setCategoryStatistics(categoryStatistics);
            statistics.setMonthlyStatistics(monthlyStatistics);
            statistics.setDailyStatistics(dailyStatistics);
            
            return statistics;
        } catch (Exception e) {
            log.error("获取统计数据失败", e);
            throw new RuntimeException("获取统计数据失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ExpenseCategory> getAllCategories() {
        return Arrays.asList(ExpenseCategory.values());
    }
    
    /**
     * 将Expense实体转换为ExpenseDTO
     */
    private ExpenseDTO convertToDTO(Expense expense) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(expense.getId());
        dto.setAmount(expense.getAmount());
        dto.setCategoryId(expense.getCategory() != null ? expense.getCategory().getId() : null);
        dto.setCategoryName(expense.getCategoryName());
        dto.setCategoryIcon(expense.getCategory() != null ? expense.getCategory().getIcon() : null);
        dto.setDescription(expense.getDescription());
        dto.setExpenseDate(expense.getExpenseDate());
        dto.setPayer(expense.getPayer());
        dto.setIsPublicExpense(expense.getIsPublicExpense());
        dto.setUserId(expense.getUserId());
        dto.setCreatedAt(expense.getCreatedAt());
        dto.setUpdatedAt(expense.getUpdatedAt());
        return dto;
    }
}