package com.exam.common;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页响应结果类
 */
@Data
public class PageResult<T> {
    
    private List<T> content;
    
    private Integer page;
    
    private Integer size;
    
    private Long totalElements;
    
    private Integer totalPages;
    
    private Boolean first;
    
    private Boolean last;
    
    private Boolean hasNext;
    
    private Boolean hasPrevious;
    
    public PageResult() {}
    
    public PageResult(Page<T> page) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
    }
    
    public static <T> PageResult<T> of(Page<T> page) {
        return new PageResult<>(page);
    }
}