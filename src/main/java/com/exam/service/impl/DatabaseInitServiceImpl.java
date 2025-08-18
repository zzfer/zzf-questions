package com.exam.service.impl;

import com.exam.entity.Expense;
import com.exam.entity.Question;
import com.exam.entity.QuestionOption;
import com.exam.enums.Difficulty;
import com.exam.enums.QuestionStatus;
import com.exam.enums.QuestionType;
import com.exam.repository.ExpenseRepository;
import com.exam.repository.QuestionRepository;
import com.exam.service.DatabaseInitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * 数据库初始化服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitServiceImpl implements DatabaseInitService {
    
    private final QuestionRepository questionRepository;
    private final ExpenseRepository expenseRepository;
    
    @Override
    @Transactional(readOnly = true)
    public boolean isDatabaseInitialized() {
        DatabaseStats stats = getDatabaseStats();
        boolean initialized = !stats.isEmpty();
        log.info("数据库初始化状态检查: {}, 统计信息: {}", 
                initialized ? "已初始化" : "未初始化", stats);
        return initialized;
    }
    
    @Override
    @Transactional
    public void forceReinitializeDatabase() {
        log.warn("开始强制重新初始化数据库...");
        
        // 清空现有数据
        clearAllData();
        
        // 重新初始化示例数据
        initializeSampleData();
        
        log.info("数据库强制重新初始化完成！");
    }
    
    @Override
    @Transactional
    public void initializeSampleData() {
        DatabaseStats stats = getDatabaseStats();
        
        if (stats.isEmpty()) {
            log.info("数据库为空，开始初始化示例数据...");
            
            // 初始化题目数据
            initializeQuestionData();
            
            // 可选：初始化记账数据
            // initializeExpenseData();
            
            log.info("示例数据初始化完成！");
        } else {
            log.info("数据库已有数据，跳过示例数据初始化。当前统计: {}", stats);
        }
    }
    
    @Override
    @Transactional
    public void clearAllData() {
        log.warn("开始清空所有数据...");
        
        // 由于外键约束，需要按顺序删除
        long optionCount = questionRepository.findAll().stream()
                .mapToLong(q -> q.getOptions().size())
                .sum();
        
        long expenseCount = expenseRepository.count();
        long questionCount = questionRepository.count();
        
        // 删除记账记录
        expenseRepository.deleteAll();
        
        // 删除题目（选项会级联删除）
        questionRepository.deleteAll();
        
        log.warn("数据清空完成！删除了 {} 道题目, {} 个选项, {} 条记账记录", 
                questionCount, optionCount, expenseCount);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DatabaseStats getDatabaseStats() {
        long questionCount = questionRepository.count();
        long expenseCount = expenseRepository.count();
        
        // 计算选项总数
        long optionCount = questionRepository.findAll().stream()
                .mapToLong(q -> q.getOptions().size())
                .sum();
        
        return new DatabaseStats(questionCount, expenseCount, optionCount);
    }
    
    /**
     * 初始化题目数据
     */
    private void initializeQuestionData() {
        log.info("开始初始化题目数据...");
        
        // 创建示例单选题
        Question singleChoiceQuestion = createSingleChoiceQuestion();
        questionRepository.save(singleChoiceQuestion);
        
        // 创建示例多选题
        Question multipleChoiceQuestion = createMultipleChoiceQuestion();
        questionRepository.save(multipleChoiceQuestion);
        
        // 创建更多示例题目
        Question javaBasicQuestion = createJavaBasicQuestion();
        questionRepository.save(javaBasicQuestion);
        
        log.info("题目数据初始化完成，共创建 {} 道题目", 3);
    }
    
    /**
     * 初始化记账数据（可选）
     */
    private void initializeExpenseData() {
        log.info("开始初始化记账数据...");
        
        Expense[] expenses = {
            createExpense(new BigDecimal("50.00"), "餐饮", "午餐", LocalDate.now().minusDays(1), "张三"),
            createExpense(new BigDecimal("120.00"), "交通", "打车费用", LocalDate.now(), "李四"),
            createExpense(new BigDecimal("200.00"), "购物", "日用品采购", LocalDate.now().minusDays(2), "王五"),
            createExpense(new BigDecimal("80.00"), "娱乐", "电影票", LocalDate.now().minusDays(3), "张三"),
            createExpense(new BigDecimal("300.00"), "医疗", "体检费用", LocalDate.now().minusDays(5), "李四")
        };
        
        for (Expense expense : expenses) {
            expenseRepository.save(expense);
        }
        
        log.info("记账数据初始化完成，共创建 {} 条记录", expenses.length);
    }
    
    private Question createSingleChoiceQuestion() {
        Question question = new Question();
        question.setTitle("Java中哪个关键字用于定义常量？");
        question.setContent("在Java编程语言中，以下哪个关键字用于定义常量？");
        question.setType(QuestionType.SINGLE_CHOICE);
        question.setDifficulty(Difficulty.EASY);
        question.setCategory("Java基础");
        question.setScore(2);
        question.setExplanation("final关键字用于定义常量，一旦赋值后不能再修改。");
        question.setStatus(QuestionStatus.ACTIVE);
        
        question.setOptions(Arrays.asList(
            createOption("A", "static", false, 1, question),
            createOption("B", "final", true, 2, question),
            createOption("C", "const", false, 3, question),
            createOption("D", "var", false, 4, question)
        ));
        
        return question;
    }
    
    private Question createMultipleChoiceQuestion() {
        Question question = new Question();
        question.setTitle("以下哪些是Java的基本数据类型？");
        question.setContent("Java语言中，以下哪些选项是基本数据类型？");
        question.setType(QuestionType.MULTIPLE_CHOICE);
        question.setDifficulty(Difficulty.MEDIUM);
        question.setCategory("Java基础");
        question.setScore(3);
        question.setExplanation("Java的基本数据类型包括：byte、short、int、long、float、double、char、boolean。");
        question.setStatus(QuestionStatus.ACTIVE);
        
        question.setOptions(Arrays.asList(
            createOption("A", "int", true, 1, question),
            createOption("B", "String", false, 2, question),
            createOption("C", "boolean", true, 3, question),
            createOption("D", "double", true, 4, question)
        ));
        
        return question;
    }
    
    private Question createJavaBasicQuestion() {
        Question question = new Question();
        question.setTitle("Java中哪个关键字用于继承？");
        question.setContent("在Java面向对象编程中，哪个关键字用于实现类的继承？");
        question.setType(QuestionType.SINGLE_CHOICE);
        question.setDifficulty(Difficulty.EASY);
        question.setCategory("Java基础");
        question.setScore(2);
        question.setExplanation("extends关键字用于类的继承，子类可以继承父类的属性和方法。");
        question.setStatus(QuestionStatus.ACTIVE);
        
        question.setOptions(Arrays.asList(
            createOption("A", "extends", true, 1, question),
            createOption("B", "implements", false, 2, question),
            createOption("C", "inherit", false, 3, question),
            createOption("D", "super", false, 4, question)
        ));
        
        return question;
    }
    
    private QuestionOption createOption(String label, String content, boolean isCorrect, int sortOrder, Question question) {
        QuestionOption option = new QuestionOption();
        option.setOptionLabel(label);
        option.setOptionContent(content);
        option.setIsCorrect(isCorrect);
        option.setSortOrder(sortOrder);
        option.setQuestion(question);
        return option;
    }
    
    private Expense createExpense(BigDecimal amount, String category, String description, LocalDate date, String payer) {
        Expense expense = new Expense();
        expense.setAmount(amount);
        expense.setCategory(category);
        expense.setDescription(description);
        expense.setExpenseDate(date);
        expense.setPayer(payer);
        return expense;
    }
}