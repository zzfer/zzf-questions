package com.exam.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 题目选项实体类
 */
@Data
@Entity
@Table(name = "question_options")
@ToString(exclude = "question")
@EqualsAndHashCode(exclude = "question")
public class QuestionOption {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "option_label", nullable = false, length = 1)
    private String optionLabel;
    
    @Column(name = "option_content", nullable = false, columnDefinition = "TEXT")
    private String optionContent;
    
    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect = false;
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    @Column(name = "created_time", updatable = false)
    private LocalDateTime createdTime;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
}