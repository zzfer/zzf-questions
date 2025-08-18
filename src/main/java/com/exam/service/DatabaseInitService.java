package com.exam.service;

/**
 * 数据库初始化服务接口
 * 提供程序化的数据库初始化控制
 */
public interface DatabaseInitService {
    
    /**
     * 检查数据库是否已初始化
     * @return true表示已初始化，false表示未初始化
     */
    boolean isDatabaseInitialized();
    
    /**
     * 强制重新初始化数据库
     * 注意：这将清空现有数据！
     */
    void forceReinitializeDatabase();
    
    /**
     * 初始化示例数据
     * 仅在数据库为空时执行
     */
    void initializeSampleData();
    
    /**
     * 清空所有数据
     * 注意：这是危险操作！
     */
    void clearAllData();
    
    /**
     * 获取数据库统计信息
     * @return 包含各表记录数的统计信息
     */
    DatabaseStats getDatabaseStats();
    
    /**
     * 数据库统计信息
     */
    class DatabaseStats {
        private long questionCount;
        private long expenseCount;
        private long optionCount;
        
        public DatabaseStats(long questionCount, long expenseCount, long optionCount) {
            this.questionCount = questionCount;
            this.expenseCount = expenseCount;
            this.optionCount = optionCount;
        }
        
        public long getQuestionCount() {
            return questionCount;
        }
        
        public long getExpenseCount() {
            return expenseCount;
        }
        
        public long getOptionCount() {
            return optionCount;
        }
        
        public boolean isEmpty() {
            return questionCount == 0 && expenseCount == 0 && optionCount == 0;
        }
        
        @Override
        public String toString() {
            return String.format("DatabaseStats{questions=%d, expenses=%d, options=%d}", 
                    questionCount, expenseCount, optionCount);
        }
    }
}