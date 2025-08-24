package com.exam.service;

import java.math.BigDecimal;

/**
 * 黄金价格服务接口
 */
public interface GoldPriceService {
    
    /**
     * 获取当前黄金价格（美元/盎司）
     * @return 黄金价格
     */
    BigDecimal getCurrentGoldPrice();
    
    /**
     * 获取当前黄金价格（人民币/克）
     * @return 黄金价格
     */
    BigDecimal getCurrentGoldPriceInCNY();
}