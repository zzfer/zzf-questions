package com.zzf.dto;

import java.time.LocalDateTime;

public class CategoryDTO {
    private Long id;
    private String name;
    private String icon;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer sortOrder;

    // 默认构造函数
    public CategoryDTO() {}

    // 带参数构造函数
    public CategoryDTO(Long id, String name, String icon, String description, LocalDateTime createdAt, LocalDateTime updatedAt, Integer sortOrder) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.sortOrder = sortOrder;
    }

    // 简化构造函数（用于创建新分类）
    public CategoryDTO(String name, String icon, String description) {
        this.name = name;
        this.icon = icon;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}