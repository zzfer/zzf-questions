package com.exam;

import com.exam.entity.AssetRecord;
import com.exam.entity.Expense;
import com.exam.repository.AssetRecordRepository;
import com.exam.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AssetRecordServiceImpl implements AssetRecordService {
    
    @Autowired
    private AssetRecordRepository assetRecordRepository;
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Override
    public AssetRecord createAssetRecord(AssetRecord assetRecord) {
        // 如果金额为null，设置默认值为0
        if (assetRecord.getAmount() == null) {
            assetRecord.setAmount(BigDecimal.ZERO);
        }
        
        if (!validateAssetRecord(assetRecord)) {
            throw new IllegalArgumentException("Invalid asset record data");
        }
        return assetRecordRepository.save(assetRecord);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<AssetRecord> getAssetRecordById(Long id) {
        return assetRecordRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AssetRecord> getAllAssetRecords() {
        return assetRecordRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AssetRecord> getAssetRecordsByUserId(Long userId) {
        return assetRecordRepository.findByUserIdOrderByRecordDateDesc(userId);
    }
    
    @Override
    public AssetRecord updateAssetRecord(Long id, AssetRecord assetRecord) {
        Optional<AssetRecord> existingRecord = assetRecordRepository.findById(id);
        if (existingRecord.isPresent()) {
            AssetRecord record = existingRecord.get();
            record.setRecordType(assetRecord.getRecordType());
            // 如果金额为null，设置默认值为0
            record.setAmount(assetRecord.getAmount() != null ? assetRecord.getAmount() : BigDecimal.ZERO);
            record.setDescription(assetRecord.getDescription());
            record.setRecordDate(assetRecord.getRecordDate());
            record.setGoldWeight(assetRecord.getGoldWeight());
            record.setAnnualInterestRate(assetRecord.getAnnualInterestRate());
            record.setOwner(assetRecord.getOwner());
            
            if (!validateAssetRecord(record)) {
                throw new IllegalArgumentException("Invalid asset record data");
            }
            
            return assetRecordRepository.save(record);
        }
        throw new RuntimeException("Asset record not found with id: " + id);
    }
    
    @Override
    public void deleteAssetRecord(Long id) {
        if (!assetRecordRepository.existsById(id)) {
            throw new RuntimeException("Asset record not found with id: " + id);
        }
        assetRecordRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AssetRecord> getAssetRecordsByUserIdAndType(Long userId, String recordType) {
        return assetRecordRepository.findByUserIdAndRecordTypeOrderByRecordDateDesc(userId, recordType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AssetRecord> getAssetRecordsByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return assetRecordRepository.findByUserIdAndRecordDateBetweenOrderByRecordDateDesc(userId, startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AssetRecord> getAssetRecordsByOwner(String owner) {
        return assetRecordRepository.findByOwnerOrderByRecordDateDesc(owner);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AssetRecord> getAssetRecordsByOwnerAndType(String owner, String recordType) {
        return assetRecordRepository.findByOwnerAndRecordTypeOrderByRecordDateDesc(owner, recordType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AssetRecord> getAssetRecordsByOwnerAndDateRange(String owner, LocalDate startDate, LocalDate endDate) {
        return assetRecordRepository.findByOwnerAndRecordDateBetweenOrderByRecordDateDesc(owner, startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalAssetsByUserId(Long userId) {
        return assetRecordRepository.calculateTotalAssetsByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateAssetsByUserIdAndType(Long userId, String recordType) {
        return assetRecordRepository.calculateAssetsByUserIdAndType(userId, recordType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateMonthlyIncomeByUserId(Long userId) {
        // 直接返回用户的工资（一个人只会有一条工资记录）
        return assetRecordRepository.calculateSalaryByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalSalary() {
        // 计算所有人的工资总和
        return assetRecordRepository.calculateTotalSalary();
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalAssetsForAll() {
        // 计算所有人的总资产
        return assetRecordRepository.calculateTotalAssets();
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalAssetsByFilter(String recordType, String owner, LocalDate startDate, LocalDate endDate) {
        // 处理空字符串为null
        String filterRecordType = (recordType != null && recordType.trim().isEmpty()) ? null : recordType;
        String filterOwner = (owner != null && owner.trim().isEmpty()) ? null : owner;
        
        return assetRecordRepository.calculateTotalAssetsByFilter(filterRecordType, filterOwner, startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalAssetsByUserIdAndFilter(Long userId, String recordType, String owner, LocalDate startDate, LocalDate endDate) {
        // 处理空字符串为null
        String filterRecordType = (recordType != null && recordType.trim().isEmpty()) ? null : recordType;
        String filterOwner = (owner != null && owner.trim().isEmpty()) ? null : owner;
        
        return assetRecordRepository.calculateTotalAssetsByUserIdAndFilter(userId, filterRecordType, filterOwner, startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public AssetPredictionResult predictYearEndAssets(Long userId) {
        // 1. 计算当前总资产
        BigDecimal currentTotalAssets = calculateTotalAssetsByUserId(userId);
        
        // 2. 计算月均收入
        BigDecimal monthlyIncome = calculateMonthlyIncomeByUserId(userId);
        
        // 3. 计算日均支出（从支出记录中获取）
        BigDecimal dailyExpense = calculateDailyExpenseByUserId(userId);
        
        // 4. 计算到年底的剩余天数
        LocalDate now = LocalDate.now();
        LocalDate yearEnd = LocalDate.of(now.getYear(), 12, 31);
        long remainingDays = ChronoUnit.DAYS.between(now, yearEnd);
        
        // 5. 计算剩余月数
        long remainingMonths = ChronoUnit.MONTHS.between(now, yearEnd);
        
        // 6. 预计剩余收入
        BigDecimal expectedIncome = monthlyIncome.multiply(BigDecimal.valueOf(remainingMonths));
        
        // 7. 预计剩余支出
        BigDecimal expectedExpense = dailyExpense.multiply(BigDecimal.valueOf(remainingDays));
        
        // 8. 计算投资利息收入
        BigDecimal interestIncome = calculateInterestIncome(userId, remainingDays);
        
        // 9. 预测年底资产
        BigDecimal predictedYearEndAssets = currentTotalAssets
                .add(expectedIncome)
                .subtract(expectedExpense)
                .add(interestIncome);
        
        return new AssetPredictionResult(
                currentTotalAssets,
                monthlyIncome,
                dailyExpense,
                predictedYearEndAssets,
                expectedIncome,
                expectedExpense,
                interestIncome
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AssetRecord> getRecentAssetRecords(Long userId, int limit) {
        if (limit <= 10) {
            return assetRecordRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
        }
        return assetRecordRepository.findByUserIdOrderByRecordDateDesc(userId);
    }
    
    @Override
    public boolean validateAssetRecord(AssetRecord assetRecord) {
        if (assetRecord == null) {
            return false;
        }
        
        if (assetRecord.getUserId() == null || assetRecord.getUserId() <= 0) {
            return false;
        }
        
        if (assetRecord.getRecordType() == null || assetRecord.getRecordType().trim().isEmpty()) {
            return false;
        }
        
        // 金额可以为null（会设置默认值0），但不能为负数
        if (assetRecord.getAmount() != null && assetRecord.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        
        if (assetRecord.getRecordDate() == null) {
            return false;
        }
        
        if (assetRecord.getOwner() == null || assetRecord.getOwner().trim().isEmpty()) {
            return false;
        }
        
        // 验证黄金记录
        if ("gold".equals(assetRecord.getRecordType())) {
            if (assetRecord.getGoldWeight() == null || assetRecord.getGoldWeight().compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }
        }
        
        // 验证投资记录
        if ("investment".equals(assetRecord.getRecordType())) {
            if (assetRecord.getAnnualInterestRate() != null && 
                (assetRecord.getAnnualInterestRate().compareTo(BigDecimal.ZERO) < 0 || 
                 assetRecord.getAnnualInterestRate().compareTo(BigDecimal.valueOf(100)) > 0)) {
                return false;
            }
        }
        
        return true;
    }
    
    // 私有辅助方法
    private BigDecimal calculateDailyExpenseByUserId(Long userId) {
        // 计算最近30天的日均支出，按照支出统计页面的逻辑：总支出 / 实际有支出记录的天数
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        
        BigDecimal totalExpense = expenseRepository.calculateTotalExpenseByUserIdAndDateRange(userId, startDate, endDate);
        if (totalExpense == null) {
            totalExpense = BigDecimal.ZERO;
        }
        
        Long actualDays = expenseRepository.countDistinctExpenseDaysByUserIdAndDateRange(userId, startDate, endDate);
        if (actualDays == null || actualDays == 0) {
            return BigDecimal.ZERO;
        }
        
        return totalExpense.divide(BigDecimal.valueOf(actualDays), 2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculateDailyExpenseForAll() {
        // 计算所有人最近30天的日均支出，按照支出统计页面的逻辑：总支出 / 实际有支出记录的天数
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        
        BigDecimal totalExpense = expenseRepository.calculateTotalExpenseByDateRange(startDate, endDate);
        if (totalExpense == null) {
            totalExpense = BigDecimal.ZERO;
        }
        
        Long actualDays = expenseRepository.countDistinctExpenseDaysByDateRange(startDate, endDate);
        if (actualDays == null || actualDays == 0) {
            return BigDecimal.ZERO;
        }
        
        return totalExpense.divide(BigDecimal.valueOf(actualDays), 2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculateInterestIncome(Long userId, long days) {
        List<AssetRecord> investmentRecords = assetRecordRepository.findInvestmentRecordsByUserId(userId);
        BigDecimal totalInterest = BigDecimal.ZERO;
        
        for (AssetRecord record : investmentRecords) {
            if (record.getAnnualInterestRate() != null) {
                // 计算日利率
                BigDecimal dailyRate = record.getAnnualInterestRate()
                        .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP)
                        .divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP);
                
                // 计算利息收入
                BigDecimal interest = record.getAmount()
                        .multiply(dailyRate)
                        .multiply(BigDecimal.valueOf(days));
                
                totalInterest = totalInterest.add(interest);
            }
        }
        
        return totalInterest;
    }
    
    private BigDecimal calculateInterestIncomeForAll(long days) {
        List<AssetRecord> investmentRecords = assetRecordRepository.findAllInvestmentRecords();
        BigDecimal totalInterest = BigDecimal.ZERO;
        
        for (AssetRecord record : investmentRecords) {
            if (record.getAnnualInterestRate() != null) {
                // 计算日利率
                BigDecimal dailyRate = record.getAnnualInterestRate()
                        .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP)
                        .divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP);
                
                // 计算利息收入
                BigDecimal interest = record.getAmount()
                        .multiply(dailyRate)
                        .multiply(BigDecimal.valueOf(days));
                
                totalInterest = totalInterest.add(interest);
            }
        }
        
        return totalInterest;
    }
    
    @Override
    @Transactional(readOnly = true)
    public AssetPredictionResult predictYearEndAssetsForAll() {
        // 1. 计算当前总资产（所有人）
        BigDecimal currentTotalAssets = assetRecordRepository.calculateTotalAssets();
        
        // 2. 计算月均收入（所有人的工资总和）
        BigDecimal monthlyIncome = calculateTotalSalary();
        
        // 3. 计算日均支出（所有人）
        BigDecimal dailyExpense = calculateDailyExpenseForAll();
        
        // 4. 计算到年底的剩余天数
        LocalDate now = LocalDate.now();
        LocalDate yearEnd = LocalDate.of(now.getYear(), 12, 31);
        long remainingDays = ChronoUnit.DAYS.between(now, yearEnd);
        
        // 5. 计算剩余月数
        long remainingMonths = ChronoUnit.MONTHS.between(now, yearEnd);
        
        // 6. 预计剩余收入
        BigDecimal expectedIncome = monthlyIncome.multiply(BigDecimal.valueOf(remainingMonths));
        
        // 7. 预计剩余支出
        BigDecimal expectedExpense = dailyExpense.multiply(BigDecimal.valueOf(remainingDays));
        
        // 8. 计算投资利息收入（所有人）
        BigDecimal interestIncome = calculateInterestIncomeForAll(remainingDays);
        
        // 9. 预测年底资产
        BigDecimal predictedYearEndAssets = currentTotalAssets
                .add(expectedIncome)
                .subtract(expectedExpense)
                .add(interestIncome);

        
        return new AssetPredictionResult(
                currentTotalAssets,
                monthlyIncome,
                dailyExpense,
                predictedYearEndAssets,
                expectedIncome,
                expectedExpense,
                interestIncome
        );
    }
}