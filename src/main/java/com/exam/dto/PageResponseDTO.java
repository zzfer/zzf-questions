package com.exam.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 分页响应数据传输对象
 * @param <T> 分页内容类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDTO<T> {
    
    /**
     * 分页内容
     */
    private List<T> content;
    
    /**
     * 总元素数量
     */
    private Long totalElements;
    
    /**
     * 总页数
     */
    private Integer totalPages;
    
    /**
     * 当前页码（从0开始）
     */
    private Integer currentPage;
    
    /**
     * 每页大小
     */
    private Integer pageSize;
    
    /**
     * 是否为第一页
     */
    private Boolean first;
    
    /**
     * 是否为最后一页
     */
    private Boolean last;
    
    /**
     * 创建分页响应
     * @param content 分页内容
     * @param totalElements 总元素数量
     * @param currentPage 当前页码
     * @param pageSize 每页大小
     * @param <T> 内容类型
     * @return 分页响应
     */
    public static <T> PageResponseDTO<T> of(List<T> content, long totalElements, int currentPage, int pageSize) {
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        boolean first = currentPage == 0;
        boolean last = currentPage >= totalPages - 1;
        
        return new PageResponseDTO<>(
            content,
            totalElements,
            totalPages,
            currentPage,
            pageSize,
            first,
            last
        );
    }
}