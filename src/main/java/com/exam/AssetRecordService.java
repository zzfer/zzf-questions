package com.exam;

import com.exam.entity.AssetRecord;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AssetRecordService {
    
    // 基本CRUD操作
    AssetRecord createAssetRecord(AssetRecord assetRecord);
    
    Optional<AssetRecord> getAssetRecordById(Long id);
    
    List<AssetRecord> getAllAssetRecords();
    
    List<AssetRecord> getAssetRecordsByUserId(Long userId);
    
    AssetRecord updateAssetRecord(Long id, AssetRecord assetRecord);
    
    void deleteAssetRecord(Long id);
    
    // 业务查询方法
    List<AssetRecord> getAssetRecordsByUserIdAndType(Long userId, String recordType);
    
    List<AssetRecord> getAssetRecordsByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);
    
    // 按所属人查询方法
    List<AssetRecord> getAssetRecordsByOwner(String owner);
    
    List<AssetRecord> getAssetRecordsByOwnerAndType(String owner, String recordType);
    
    List<AssetRecord> getAssetRecordsByOwnerAndDateRange(String owner, LocalDate startDate, LocalDate endDate);
    
    // 统计计算方法
    BigDecimal calculateTotalAssetsByUserId(Long userId);
    
    BigDecimal calculateAssetsByUserIdAndType(Long userId, String recordType);
    
    BigDecimal calculateMonthlyIncomeByUserId(Long userId);
    
    BigDecimal calculateTotalSalary();
    
    // 计算所有人总资产
    BigDecimal calculateTotalAssetsForAll();
    
    // 年底资产预测
    AssetPredictionResult predictYearEndAssets(Long userId);
    
    // 全部用户年底资产预测
    AssetPredictionResult predictYearEndAssetsForAll();
    
    // 获取用户最近记录
    List<AssetRecord> getRecentAssetRecords(Long userId, int limit);
    
    // 验证方法
    boolean validateAssetRecord(AssetRecord assetRecord);
    
    // 内部类：资产预测结果
    class AssetPredictionResult {
        private BigDecimal currentTotalAssets;
        private BigDecimal monthlyIncome;
        private BigDecimal dailyExpense;
        private BigDecimal predictedYearEndAssets;
        private BigDecimal expectedIncome;
        private BigDecimal expectedExpense;
        private BigDecimal interestIncome;
        
        // Constructors
        public AssetPredictionResult() {}
        
        public AssetPredictionResult(BigDecimal currentTotalAssets, BigDecimal monthlyIncome, 
                                   BigDecimal dailyExpense, BigDecimal predictedYearEndAssets,
                                   BigDecimal expectedIncome, BigDecimal expectedExpense, 
                                   BigDecimal interestIncome) {
            this.currentTotalAssets = currentTotalAssets;
            this.monthlyIncome = monthlyIncome;
            this.dailyExpense = dailyExpense;
            this.predictedYearEndAssets = predictedYearEndAssets;
            this.expectedIncome = expectedIncome;
            this.expectedExpense = expectedExpense;
            this.interestIncome = interestIncome;
        }
        
        // Getters and Setters
        public BigDecimal getCurrentTotalAssets() {
            return currentTotalAssets;
        }
        
        public void setCurrentTotalAssets(BigDecimal currentTotalAssets) {
            this.currentTotalAssets = currentTotalAssets;
        }
        
        public BigDecimal getMonthlyIncome() {
            return monthlyIncome;
        }
        
        public void setMonthlyIncome(BigDecimal monthlyIncome) {
            this.monthlyIncome = monthlyIncome;
        }
        
        public BigDecimal getDailyExpense() {
            return dailyExpense;
        }
        
        public void setDailyExpense(BigDecimal dailyExpense) {
            this.dailyExpense = dailyExpense;
        }
        
        public BigDecimal getPredictedYearEndAssets() {
            return predictedYearEndAssets;
        }
        
        public void setPredictedYearEndAssets(BigDecimal predictedYearEndAssets) {
            this.predictedYearEndAssets = predictedYearEndAssets;
        }
        
        public BigDecimal getExpectedIncome() {
            return expectedIncome;
        }
        
        public void setExpectedIncome(BigDecimal expectedIncome) {
            this.expectedIncome = expectedIncome;
        }
        
        public BigDecimal getExpectedExpense() {
            return expectedExpense;
        }
        
        public void setExpectedExpense(BigDecimal expectedExpense) {
            this.expectedExpense = expectedExpense;
        }
        
        public BigDecimal getInterestIncome() {
            return interestIncome;
        }
        
        public void setInterestIncome(BigDecimal interestIncome) {
            this.interestIncome = interestIncome;
        }
    }
}