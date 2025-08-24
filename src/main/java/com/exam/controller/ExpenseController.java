package com.exam.controller;

import com.exam.dto.ApiResponseDTO;
import com.exam.dto.ExpenseDTO;
import com.exam.dto.ExpenseStatisticsDTO;
import com.exam.dto.PageResponseDTO;
import com.exam.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class ExpenseController {
    
    private final ExpenseService expenseService;
    
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ExpenseDTO>> createExpense(@RequestBody ExpenseDTO expenseDTO) {
        log.info("创建记账记录: {}", expenseDTO);
        
        ExpenseDTO savedExpense = expenseService.createExpense(expenseDTO);
        
        return ResponseEntity.ok(ApiResponseDTO.success("记账记录创建成功", savedExpense));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<ExpenseDTO>>> getExpenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "payer", required = false) String payer,
            @RequestParam(value = "isPublicExpense", required = false) Boolean isPublicExpense) {
        
        log.info("查询记账记录，页码: {}, 大小: {}, 开始日期: {}, 结束日期: {}, 分类: {}, 付款人: {}, 公共支出: {}", 
                page, size, startDate, endDate, categoryName, payer, isPublicExpense);
        
        List<ExpenseDTO> allExpenses;
        
        // 设置默认日期范围
        LocalDate start = startDate != null ? LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE) : null;
        
        // 根据筛选条件查询
        if (categoryName != null || payer != null || isPublicExpense != null) {
            // 使用多条件查询，需要在ExpenseService中添加相应方法
            allExpenses = expenseService.getExpensesByFilters(start, end, categoryName, payer, isPublicExpense);
        } else if (start != null && end != null) {
            allExpenses = expenseService.getExpensesByDateRange(start, end);
        } else {
            allExpenses = expenseService.getAllExpenses();
        }
        
        // 简单的分页
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, allExpenses.size());
        List<ExpenseDTO> pageContent = startIndex < allExpenses.size() ? 
            allExpenses.subList(startIndex, endIndex) : new ArrayList<>();
        
        PageResponseDTO<ExpenseDTO> pageData = PageResponseDTO.of(
            pageContent, 
            allExpenses.size(), 
            page, 
            size
        );
        
        return ResponseEntity.ok(ApiResponseDTO.success(pageData));
    }
    
    @GetMapping("/recent")
    public ResponseEntity<ApiResponseDTO<List<ExpenseDTO>>> getRecentExpenses() {
        log.info("查询最近的记账记录");
        
        List<ExpenseDTO> recentExpenses = expenseService.getRecentExpenses();
        
        return ResponseEntity.ok(ApiResponseDTO.success(recentExpenses));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ExpenseDTO>> updateExpense(
            @PathVariable Long id, 
            @RequestBody ExpenseDTO expenseDTO) {
        
        log.info("更新记账记录，ID: {}, 数据: {}", id, expenseDTO);
        
        try {
            ExpenseDTO updatedExpense = expenseService.updateExpense(id, expenseDTO);
            
            return ResponseEntity.ok(ApiResponseDTO.success("记账记录更新成功", updatedExpense));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteExpense(@PathVariable Long id) {
        log.info("删除记账记录，ID: {}", id);
        
        try {
            expenseService.deleteExpense(id);
            
            return ResponseEntity.ok(ApiResponseDTO.<Void>success("记账记录删除成功", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error(e.getMessage()));
        }
    }
    
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponseDTO<ExpenseStatisticsDTO>> getStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "payer", required = false) String payer,
            @RequestParam(value = "isPublicExpense", required = false) Boolean isPublicExpense) {
        
        log.info("获取统计数据，开始日期: {}, 结束日期: {}, 分类: {}, 付款人: {}, 公共支出: {}", startDate, endDate, categoryName, payer, isPublicExpense);
        
        LocalDate start = startDate != null ? LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE) : LocalDate.now().minusMonths(1);
        LocalDate end = endDate != null ? LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE) : LocalDate.now();
        
        try {
            ExpenseStatisticsDTO statistics;
            if (categoryName != null || payer != null || isPublicExpense != null) {
                // 使用多条件查询
                statistics = expenseService.getStatistics(start, end, categoryName, payer, isPublicExpense);
            } else {
                // 使用原有的查询方法
                statistics = expenseService.getStatistics(start, end);
            }
            
            return ResponseEntity.ok(ApiResponseDTO.success(statistics));
        } catch (Exception e) {
            log.error("获取统计数据失败", e);
            return ResponseEntity.badRequest().body(ApiResponseDTO.error("获取统计数据失败: " + e.getMessage()));
        }
    }
    

}