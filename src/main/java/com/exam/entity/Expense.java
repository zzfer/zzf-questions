package com.exam.entity;

import com.zzf.entity.Category;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 记账实体类
 */
@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 金额
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    /**
     * 分类关联
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    /**
     * 分类名称（冗余字段，用于向后兼容和查询优化）
     */
    @Column(name = "category_name", length = 50)
    private String categoryName;
    
    /**
     * 备注
     */
    @Column(length = 500)
    private String description;
    
    /**
     * 记账日期
     */
    @Column(nullable = false)
    private LocalDate expenseDate;
    
    /**
     * 支出人
     */
    @Column(nullable = false, length = 20)
    private String payer;
    
    /**
     * 是否公共支出
     */
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isPublicExpense = false;
    
    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    /**
     * 用户ID（预留字段，如果需要多用户支持）
     */
    @Column
    private Long userId;
}