package com.exam.controller;

import com.exam.dto.ExpenseDTO;
import com.exam.dto.ExpenseStatisticsDTO;
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
    public ResponseEntity<Map<String, Object>> createExpense(@RequestBody ExpenseDTO expenseDTO) {
        log.info("创建记账记录: {}", expenseDTO);
        
        ExpenseDTO savedExpense = expenseService.createExpense(expenseDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "记账记录创建成功");
        response.put("data", savedExpense);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getExpenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        log.info("查询记账记录，页码: {}, 大小: {}, 开始日期: {}, 结束日期: {}", page, size, startDate, endDate);
        
        List<ExpenseDTO> allExpenses;
        
        // 根据日期范围查询
        if (startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
            allExpenses = expenseService.getExpensesByDateRange(start, end);
        } else {
            allExpenses = expenseService.getAllExpenses();
        }
        
        // 简单的分页
        int start = page * size;
        int end = Math.min(start + size, allExpenses.size());
        List<ExpenseDTO> pageContent = start < allExpenses.size() ? 
            allExpenses.subList(start, end) : new ArrayList<>();
        
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("content", pageContent);
        pageData.put("totalElements", allExpenses.size());
        pageData.put("totalPages", (int) Math.ceil((double) allExpenses.size() / size));
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", pageData);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<Map<String, Object>> getRecentExpenses() {
        log.info("查询最近的记账记录");
        
        List<ExpenseDTO> recentExpenses = expenseService.getRecentExpenses();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", recentExpenses);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateExpense(
            @PathVariable Long id, 
            @RequestBody ExpenseDTO expenseDTO) {
        
        log.info("更新记账记录，ID: {}, 数据: {}", id, expenseDTO);
        
        try {
            ExpenseDTO updatedExpense = expenseService.updateExpense(id, expenseDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "记账记录更新成功");
            response.put("data", updatedExpense);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteExpense(@PathVariable Long id) {
        log.info("删除记账记录，ID: {}", id);
        
        try {
            expenseService.deleteExpense(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "记账记录删除成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        log.info("获取统计数据，开始日期: {}, 结束日期: {}", startDate, endDate);
        
        LocalDate start = startDate != null ? LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE) : LocalDate.now().minusMonths(1);
        LocalDate end = endDate != null ? LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE) : LocalDate.now();
        
        try {
            ExpenseStatisticsDTO statistics = expenseService.getStatistics(start, end);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", statistics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取统计数据失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取统计数据失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    

}