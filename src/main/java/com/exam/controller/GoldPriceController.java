package com.exam.controller;

import com.exam.service.GoldPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 黄金价格控制器
 */
@RestController
@RequestMapping("/api/gold-price")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class GoldPriceController {
    
    private final GoldPriceService goldPriceService;
    
    /**
     * 获取当前黄金价格（美元/盎司）
     */
    @GetMapping("/usd")
    public ResponseEntity<Map<String, Object>> getCurrentGoldPriceUSD() {
        try {
            log.info("获取黄金价格（USD/oz）");
            
            BigDecimal goldPrice = goldPriceService.getCurrentGoldPrice();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("price", goldPrice);
            response.put("currency", "USD");
            response.put("unit", "oz");
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("获取黄金价格失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取黄金价格失败");
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 获取当前黄金价格（人民币/克）
     */
    @GetMapping("/cny")
    public ResponseEntity<Map<String, Object>> getCurrentGoldPriceCNY() {
        try {
            log.info("获取黄金价格（CNY/g）");
            
            BigDecimal goldPrice = goldPriceService.getCurrentGoldPriceInCNY();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("price", goldPrice);
            response.put("currency", "CNY");
            response.put("unit", "g");
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("获取黄金价格失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取黄金价格失败");
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
}