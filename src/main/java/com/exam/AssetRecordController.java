package com.exam;

import com.exam.entity.AssetRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/asset-records")
@CrossOrigin(origins = "*")
public class AssetRecordController {
    
    @Autowired
    private AssetRecordService assetRecordService;
    
    // 创建资产记录
    @PostMapping
    public ResponseEntity<AssetRecord> createAssetRecord(@RequestBody AssetRecord assetRecord) {
        try {
            AssetRecord createdRecord = assetRecordService.createAssetRecord(assetRecord);
            return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 获取所有资产记录
    @GetMapping
    public ResponseEntity<List<AssetRecord>> getAllAssetRecords() {
        try {
            List<AssetRecord> records = assetRecordService.getAllAssetRecords();
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 根据ID获取资产记录
    @GetMapping("/{id}")
    public ResponseEntity<AssetRecord> getAssetRecordById(@PathVariable Long id) {
        try {
            Optional<AssetRecord> record = assetRecordService.getAssetRecordById(id);
            if (record.isPresent()) {
                return new ResponseEntity<>(record.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 根据用户ID获取资产记录
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AssetRecord>> getAssetRecordsByUserId(@PathVariable Long userId) {
        try {
            List<AssetRecord> records = assetRecordService.getAssetRecordsByUserId(userId);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 根据用户ID和记录类型获取资产记录
    @GetMapping("/user/{userId}/type/{recordType}")
    public ResponseEntity<List<AssetRecord>> getAssetRecordsByUserIdAndType(
            @PathVariable Long userId, 
            @PathVariable String recordType) {
        try {
            List<AssetRecord> records = assetRecordService.getAssetRecordsByUserIdAndType(userId, recordType);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 根据用户ID和日期范围获取资产记录
    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<AssetRecord>> getAssetRecordsByUserIdAndDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<AssetRecord> records = assetRecordService.getAssetRecordsByUserIdAndDateRange(userId, start, end);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    // 根据所属人获取资产记录
    @GetMapping("/owner/{owner}")
    public ResponseEntity<List<AssetRecord>> getAssetRecordsByOwner(@PathVariable String owner) {
        try {
            List<AssetRecord> records = assetRecordService.getAssetRecordsByOwner(owner);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 根据所属人和记录类型获取资产记录
    @GetMapping("/owner/{owner}/type/{recordType}")
    public ResponseEntity<List<AssetRecord>> getAssetRecordsByOwnerAndType(
            @PathVariable String owner, 
            @PathVariable String recordType) {
        try {
            List<AssetRecord> records = assetRecordService.getAssetRecordsByOwnerAndType(owner, recordType);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 根据所属人和日期范围获取资产记录
    @GetMapping("/owner/{owner}/date-range")
    public ResponseEntity<List<AssetRecord>> getAssetRecordsByOwnerAndDateRange(
            @PathVariable String owner,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<AssetRecord> records = assetRecordService.getAssetRecordsByOwnerAndDateRange(owner, start, end);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    // 更新资产记录
    @PutMapping("/{id}")
    public ResponseEntity<AssetRecord> updateAssetRecord(
            @PathVariable Long id, 
            @RequestBody AssetRecord assetRecord) {
        try {
            AssetRecord updatedRecord = assetRecordService.updateAssetRecord(id, assetRecord);
            return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 删除资产记录
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssetRecord(@PathVariable Long id) {
        try {
            assetRecordService.deleteAssetRecord(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 计算用户总资产
    @GetMapping("/user/{userId}/total-assets")
    public ResponseEntity<BigDecimal> calculateTotalAssetsByUserId(@PathVariable Long userId) {
        try {
            BigDecimal totalAssets = assetRecordService.calculateTotalAssetsByUserId(userId);
            return new ResponseEntity<>(totalAssets, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 计算所有人总资产
    @GetMapping("/total-assets")
    public ResponseEntity<BigDecimal> calculateTotalAssetsForAll() {
        try {
            BigDecimal totalAssets = assetRecordService.calculateTotalAssetsForAll();
            return new ResponseEntity<>(totalAssets, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 根据类型计算用户资产
    @GetMapping("/user/{userId}/assets-by-type/{recordType}")
    public ResponseEntity<BigDecimal> calculateAssetsByUserIdAndType(
            @PathVariable Long userId, 
            @PathVariable String recordType) {
        try {
            BigDecimal assets = assetRecordService.calculateAssetsByUserIdAndType(userId, recordType);
            return new ResponseEntity<>(assets, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 计算用户月均收入（工资）
    @GetMapping("/user/{userId}/monthly-income")
    public ResponseEntity<BigDecimal> calculateMonthlyIncomeByUserId(@PathVariable Long userId) {
        try {
            BigDecimal monthlyIncome = assetRecordService.calculateMonthlyIncomeByUserId(userId);
            return new ResponseEntity<>(monthlyIncome, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 计算所有人的月均收入（工资总和）
    @GetMapping("/monthly-income")
    public ResponseEntity<BigDecimal> calculateTotalSalary() {
        try {
            BigDecimal totalSalary = assetRecordService.calculateTotalSalary();
            return new ResponseEntity<>(totalSalary, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 年底资产预测（指定用户）
    @GetMapping("/user/{userId}/year-end-prediction")
    public ResponseEntity<AssetRecordService.AssetPredictionResult> predictYearEndAssets(@PathVariable Long userId) {
        try {
            AssetRecordService.AssetPredictionResult prediction = assetRecordService.predictYearEndAssets(userId);
            return new ResponseEntity<>(prediction, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 年底资产预测（所有人）
    @GetMapping("/year-end-prediction")
    public ResponseEntity<AssetRecordService.AssetPredictionResult> predictYearEndAssetsForAll() {
        try {
            AssetRecordService.AssetPredictionResult prediction = assetRecordService.predictYearEndAssetsForAll();
            return new ResponseEntity<>(prediction, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 获取用户最近的资产记录
    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<List<AssetRecord>> getRecentAssetRecords(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<AssetRecord> records = assetRecordService.getRecentAssetRecords(userId, limit);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}