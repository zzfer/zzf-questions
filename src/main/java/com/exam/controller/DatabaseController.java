package com.exam.controller;

import com.exam.common.Result;
import com.exam.service.DatabaseInitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 数据库管理控制器
 * 提供数据库初始化和管理的API接口
 */
@RestController
@RequestMapping("/api/admin/database")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class DatabaseController {
    
    private final DatabaseInitService databaseInitService;
    
    /**
     * 检查数据库初始化状态
     */
    @GetMapping("/status")
    public Result<DatabaseStatus> getDatabaseStatus() {
        log.info("检查数据库状态");
        
        boolean initialized = databaseInitService.isDatabaseInitialized();
        DatabaseInitService.DatabaseStats stats = databaseInitService.getDatabaseStats();
        
        DatabaseStatus status = new DatabaseStatus(
            initialized,
            stats.getQuestionCount(),
            stats.getExpenseCount(),
            stats.getOptionCount()
        );
        
        return Result.success(status);
    }
    
    /**
     * 获取数据库统计信息
     */
    @GetMapping("/stats")
    public Result<DatabaseInitService.DatabaseStats> getDatabaseStats() {
        log.info("获取数据库统计信息");
        
        DatabaseInitService.DatabaseStats stats = databaseInitService.getDatabaseStats();
        return Result.success(stats);
    }
    
    /**
     * 初始化示例数据
     */
    @PostMapping("/init-sample")
    public Result<String> initializeSampleData() {
        log.info("请求初始化示例数据");
        
        try {
            databaseInitService.initializeSampleData();
            return Result.success("示例数据初始化成功");
        } catch (Exception e) {
            log.error("示例数据初始化失败", e);
            return Result.error("示例数据初始化失败: " + e.getMessage());
        }
    }
    
    /**
     * 强制重新初始化数据库
     * 警告：这将清空所有现有数据！
     */
    @PostMapping("/force-reinit")
    public Result<String> forceReinitializeDatabase() {
        log.warn("请求强制重新初始化数据库");
        
        try {
            databaseInitService.forceReinitializeDatabase();
            return Result.success("数据库强制重新初始化成功");
        } catch (Exception e) {
            log.error("数据库强制重新初始化失败", e);
            return Result.error("数据库强制重新初始化失败: " + e.getMessage());
        }
    }
    
    /**
     * 清空所有数据
     * 警告：这是危险操作！
     */
    @DeleteMapping("/clear-all")
    public Result<String> clearAllData() {
        log.warn("请求清空所有数据");
        
        try {
            databaseInitService.clearAllData();
            return Result.success("所有数据清空成功");
        } catch (Exception e) {
            log.error("清空数据失败", e);
            return Result.error("清空数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 数据库状态响应类
     */
    public static class DatabaseStatus {
        private boolean initialized;
        private long questionCount;
        private long expenseCount;
        private long optionCount;
        
        public DatabaseStatus(boolean initialized, long questionCount, long expenseCount, long optionCount) {
            this.initialized = initialized;
            this.questionCount = questionCount;
            this.expenseCount = expenseCount;
            this.optionCount = optionCount;
        }
        
        // Getters
        public boolean isInitialized() {
            return initialized;
        }
        
        public long getQuestionCount() {
            return questionCount;
        }
        
        public long getExpenseCount() {
            return expenseCount;
        }
        
        public long getOptionCount() {
            return optionCount;
        }
    }
}