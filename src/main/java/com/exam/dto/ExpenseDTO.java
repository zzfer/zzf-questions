package com.exam.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 记账数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {
    
    private Long id;
    
    /**
     * 金额
     */
    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    @Digits(integer = 8, fraction = 2, message = "金额格式不正确")
    private BigDecimal amount;
    
    /**
     * 分类ID
     */
    @NotNull(message = "分类不能为空")
    private Long categoryId;
    
    /**
     * 分类名称（用于显示）
     */
    private String categoryName;
    
    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String description;
    
    /**
     * 记账日期
     */
    @NotNull(message = "记账日期不能为空")
    private LocalDate expenseDate;
    
    /**
     * 支出人
     */
    @NotBlank(message = "支出人不能为空")
    @Size(max = 20, message = "支出人长度不能超过20个字符")
    private String payer;
    
    /**
     * 是否公共支出
     */
    private Boolean isPublicExpense = false;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 用户ID
     */
    private Long userId;
}