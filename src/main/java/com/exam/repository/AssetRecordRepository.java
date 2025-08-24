package com.exam.repository;

import com.exam.entity.AssetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssetRecordRepository extends JpaRepository<AssetRecord, Long> {
    
    // 根据用户ID查询资产记录
    List<AssetRecord> findByUserIdOrderByRecordDateDesc(Long userId);
    
    // 根据用户ID和记录类型查询
    List<AssetRecord> findByUserIdAndRecordTypeOrderByRecordDateDesc(Long userId, String recordType);
    
    // 根据用户ID和日期范围查询
    List<AssetRecord> findByUserIdAndRecordDateBetweenOrderByRecordDateDesc(Long userId, LocalDate startDate, LocalDate endDate);
    
    // 根据所属人查询资产记录
    List<AssetRecord> findByOwnerOrderByRecordDateDesc(String owner);
    
    // 根据所属人和记录类型查询
    List<AssetRecord> findByOwnerAndRecordTypeOrderByRecordDateDesc(String owner, String recordType);
    
    // 根据所属人和日期范围查询
    List<AssetRecord> findByOwnerAndRecordDateBetweenOrderByRecordDateDesc(String owner, LocalDate startDate, LocalDate endDate);
    
    // 计算用户总资产
    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM AssetRecord a WHERE a.userId = :userId")
    BigDecimal calculateTotalAssetsByUserId(@Param("userId") Long userId);
    
    // 根据记录类型计算用户资产
    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM AssetRecord a WHERE a.userId = :userId AND a.recordType = :recordType")
    BigDecimal calculateAssetsByUserIdAndType(@Param("userId") Long userId, @Param("recordType") String recordType);
    
    // 计算用户在指定日期范围内的收入总额
    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM AssetRecord a WHERE a.userId = :userId AND a.recordType IN ('salary', 'bonus') AND a.recordDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateIncomeByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // 获取用户的投资记录（用于计算利息）
    @Query("SELECT a FROM AssetRecord a WHERE a.userId = :userId AND a.recordType = 'investment' AND a.annualInterestRate IS NOT NULL")
    List<AssetRecord> findInvestmentRecordsByUserId(@Param("userId") Long userId);
    
    // 删除用户的资产记录
    void deleteByUserId(Long userId);
    
    // 统计用户的记录数量
    long countByUserId(Long userId);
    
    // 获取用户最近的记录
    List<AssetRecord> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}