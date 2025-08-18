package com.exam.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

/**
 * 数据库配置类
 * 提供数据库初始化的配置选项和环境控制
 */
@Configuration
@Slf4j
public class DatabaseConfig {
    
    @Value("${spring.sql.init.mode:embedded}")
    private String initMode;
    
    @Value("${spring.jpa.hibernate.ddl-auto:create-drop}")
    private String ddlAuto;
    
    @Value("${spring.datasource.url}")
    private String datasourceUrl;
    
    @PostConstruct
    public void logDatabaseConfig() {
        log.info("=== 数据库配置信息 ===");
        log.info("数据源URL: {}", datasourceUrl);
        log.info("DDL模式: {}", ddlAuto);
        log.info("SQL初始化模式: {}", initMode);
        log.info("========================");
        
        // 根据配置给出提示
        if ("create-drop".equals(ddlAuto)) {
            log.warn("注意：当前使用create-drop模式，应用关闭时数据将被清空！");
        }
        
        if ("always".equals(initMode)) {
            log.info("SQL脚本将在每次启动时执行");
        } else if ("embedded".equals(initMode)) {
            log.info("SQL脚本仅在嵌入式数据库时执行");
        } else if ("never".equals(initMode)) {
            log.info("SQL脚本不会自动执行");
        }
    }
    
    /**
     * 开发环境配置
     */
    @Configuration
    @Profile("dev")
    @Slf4j
    static class DevDatabaseConfig {
        @PostConstruct
        public void devConfig() {
            log.info("开发环境：启用详细的SQL日志和数据库控制台");
        }
    }
    
    /**
     * 生产环境配置
     */
    @Configuration
    @Profile("prod")
    @Slf4j
    static class ProdDatabaseConfig {
        @PostConstruct
        public void prodConfig() {
            log.info("生产环境：建议使用持久化数据库并关闭SQL日志");
        }
    }
    
    /**
     * 测试环境配置
     */
    @Configuration
    @Profile("test")
    @Slf4j
    static class TestDatabaseConfig {
        @PostConstruct
        public void testConfig() {
            log.info("测试环境：使用内存数据库，每次测试后清理数据");
        }
    }
}