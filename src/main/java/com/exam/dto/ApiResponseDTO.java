package com.exam.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 统一API响应数据传输对象
 * @param <T> 响应数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {
    
    /**
     * 响应状态
     */
    private Boolean success;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 创建成功响应
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应
     */
    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<>(true, "操作成功", data);
    }
    
    /**
     * 创建成功响应
     * @param message 响应消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应
     */
    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return new ApiResponseDTO<>(true, message, data);
    }
    
    /**
     * 创建失败响应
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 失败响应
     */
    public static <T> ApiResponseDTO<T> error(String message) {
        return new ApiResponseDTO<>(false, message, null);
    }
    
    /**
     * 创建失败响应
     * @param message 错误消息
     * @param data 错误数据
     * @param <T> 数据类型
     * @return 失败响应
     */
    public static <T> ApiResponseDTO<T> error(String message, T data) {
        return new ApiResponseDTO<>(false, message, data);
    }
}