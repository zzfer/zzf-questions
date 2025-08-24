package com.exam.service.impl;

import com.exam.service.GoldPriceService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 黄金价格服务实现类
 */
@Service
@Slf4j
public class GoldPriceServiceImpl implements GoldPriceService {
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    // 使用免费的金价API（这里使用一个备用的免费API）
    private static final String GOLD_API_URL = "https://api.metals.live/v1/spot/gold";
    
    public GoldPriceServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public BigDecimal getCurrentGoldPrice() {
        try {
            log.info("正在获取当前黄金价格...");
            
            // 调用免费的黄金价格API
            String response = restTemplate.getForObject(GOLD_API_URL, String.class);
            
            if (response != null) {
                JsonNode jsonNode = objectMapper.readTree(response);
                double goldPrice = jsonNode.get("price").asDouble();
                
                log.info("获取到黄金价格: {} USD/oz", goldPrice);
                return BigDecimal.valueOf(goldPrice).setScale(2, RoundingMode.HALF_UP);
            }
            
        } catch (Exception e) {
            log.error("获取黄金价格失败: {}", e.getMessage());
        }
        
        // 如果API调用失败，返回一个默认价格（当前大概的金价）
        log.warn("使用默认黄金价格");
        return BigDecimal.valueOf(2000.00); // 默认价格 USD/oz
    }
    
    @Override
    public BigDecimal getCurrentGoldPriceInCNY() {
        try {
            BigDecimal goldPriceUSD = getCurrentGoldPrice();
            
            // 简化处理：使用固定汇率 1 USD = 7.2 CNY
            // 实际项目中应该调用汇率API获取实时汇率
            BigDecimal exchangeRate = BigDecimal.valueOf(7.2);
            
            // 1盎司 = 31.1035克
            BigDecimal ouncesToGrams = BigDecimal.valueOf(31.1035);
            
            // 计算人民币/克价格
            BigDecimal goldPriceCNYPerGram = goldPriceUSD
                    .multiply(exchangeRate)
                    .divide(ouncesToGrams, 2, RoundingMode.HALF_UP);
            
            log.info("黄金价格: {} CNY/g", goldPriceCNYPerGram);
            return goldPriceCNYPerGram;
            
        } catch (Exception e) {
            log.error("计算人民币黄金价格失败: {}", e.getMessage());
            // 返回默认价格（大概的金价）
            return BigDecimal.valueOf(460.00); // 默认价格 CNY/g
        }
    }
}