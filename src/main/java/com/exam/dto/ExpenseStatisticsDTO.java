package com.exam.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 记账统计数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseStatisticsDTO {
    
    /**
     * 总支出
     */
    private BigDecimal totalAmount;
    
    /**
     * 记录总数
     */
    private Long totalCount;
    
    /**
     * 按分类统计 - 饼图数据
     */
    private List<CategoryStatistic> categoryStatistics;
    
    /**
     * 按月份统计 - 条形图数据
     */
    private List<MonthlyStatistic> monthlyStatistics;
    
    /**
     * 按日期统计 - 折线图数据
     */
    private List<DailyStatistic> dailyStatistics;
    
    /**
     * 分类统计内部类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryStatistic {
        private String category;
        private BigDecimal amount;
        private Long count;
        private Double percentage;
    }
    
    /**
     * 月度统计内部类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyStatistic {
        private String month; // 格式: 2024-01
        private BigDecimal amount;
        private Long count;
    }
    
    /**
     * 日度统计内部类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyStatistic {
        private LocalDate date;
        private BigDecimal amount;
        private Long count;
    }
}