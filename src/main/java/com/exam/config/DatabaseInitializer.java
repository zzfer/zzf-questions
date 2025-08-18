package com.exam.config;

import com.zzf.entity.Category;
import com.exam.entity.Expense;
import com.exam.entity.Question;
import com.exam.entity.QuestionOption;
import com.exam.entity.User;
import com.exam.enums.Difficulty;
import com.exam.enums.QuestionStatus;
import com.exam.enums.QuestionType;
import com.zzf.repository.CategoryRepository;
import com.exam.repository.ExpenseRepository;
import com.exam.repository.QuestionRepository;
import com.exam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * 智能数据库初始化器
 * 在应用启动时检查数据库状态，根据需要进行数据初始化
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {
    
    private final QuestionRepository questionRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("开始检查数据库初始化状态...");
        
        // 检查并初始化题目数据
        initializeQuestions();
        
        // 检查并初始化记账数据（可选的示例数据）
        initializeExpenses();
        
        // 检查并初始化用户数据
        initializeUsers();
        
        // 检查并初始化分类数据
        initializeCategories();
        
        log.info("数据库初始化检查完成！");
    }
    
    /**
     * 初始化题目数据
     */
    private void initializeQuestions() {
        long questionCount = questionRepository.count();
        log.info("当前题目数量: {}", questionCount);
        
        if (questionCount == 0) {
            log.info("检测到题目表为空，开始初始化示例题目数据...");
            createSampleQuestions();
            log.info("题目数据初始化完成！");
        } else {
            log.info("题目数据已存在，跳过初始化");
        }
    }
    
    /**
     * 初始化记账数据（可选）
     */
    private void initializeExpenses() {
        long expenseCount = expenseRepository.count();
        log.info("当前记账记录数量: {}", expenseCount);
        
        // 这里可以选择是否初始化示例记账数据
        // 通常记账数据不需要初始化示例数据，用户会自己添加
        if (expenseCount == 0) {
            log.info("记账表为空，开始初始化示例记账数据...");
            createSampleExpenses();
            log.info("记账数据初始化完成！");
        } else {
            log.info("记账数据已存在，当前记录数: {}", expenseCount);
        }
    }
    
    /**
     * 创建示例题目数据
     */
    private void createSampleQuestions() {
        // 创建示例单选题
        Question singleChoiceQuestion = new Question();
        singleChoiceQuestion.setTitle("Java中哪个关键字用于定义常量？");
        singleChoiceQuestion.setContent("在Java编程语言中，以下哪个关键字用于定义常量？");
        singleChoiceQuestion.setType(QuestionType.SINGLE_CHOICE);
        singleChoiceQuestion.setDifficulty(Difficulty.EASY);
        singleChoiceQuestion.setCategory("Java基础");
        singleChoiceQuestion.setScore(2);
        singleChoiceQuestion.setExplanation("final关键字用于定义常量，一旦赋值后不能再修改。");
        singleChoiceQuestion.setStatus(QuestionStatus.ACTIVE);
        
        // 创建选项
        QuestionOption option1 = new QuestionOption();
        option1.setOptionLabel("A");
        option1.setOptionContent("static");
        option1.setIsCorrect(false);
        option1.setSortOrder(1);
        option1.setQuestion(singleChoiceQuestion);
        
        QuestionOption option2 = new QuestionOption();
        option2.setOptionLabel("B");
        option2.setOptionContent("final");
        option2.setIsCorrect(true);
        option2.setSortOrder(2);
        option2.setQuestion(singleChoiceQuestion);
        
        QuestionOption option3 = new QuestionOption();
        option3.setOptionLabel("C");
        option3.setOptionContent("const");
        option3.setIsCorrect(false);
        option3.setSortOrder(3);
        option3.setQuestion(singleChoiceQuestion);
        
        QuestionOption option4 = new QuestionOption();
        option4.setOptionLabel("D");
        option4.setOptionContent("var");
        option4.setIsCorrect(false);
        option4.setSortOrder(4);
        option4.setQuestion(singleChoiceQuestion);
        
        singleChoiceQuestion.setOptions(Arrays.asList(option1, option2, option3, option4));
        
        // 创建示例多选题
        Question multipleChoiceQuestion = new Question();
        multipleChoiceQuestion.setTitle("以下哪些是Java的基本数据类型？");
        multipleChoiceQuestion.setContent("Java语言中，以下哪些选项是基本数据类型？");
        multipleChoiceQuestion.setType(QuestionType.MULTIPLE_CHOICE);
        multipleChoiceQuestion.setDifficulty(Difficulty.MEDIUM);
        multipleChoiceQuestion.setCategory("Java基础");
        multipleChoiceQuestion.setScore(3);
        multipleChoiceQuestion.setExplanation("Java的基本数据类型包括：byte、short、int、long、float、double、char、boolean。");
        multipleChoiceQuestion.setStatus(QuestionStatus.ACTIVE);
        
        // 创建多选题选项
        QuestionOption multiOption1 = new QuestionOption();
        multiOption1.setOptionLabel("A");
        multiOption1.setOptionContent("int");
        multiOption1.setIsCorrect(true);
        multiOption1.setSortOrder(1);
        multiOption1.setQuestion(multipleChoiceQuestion);
        
        QuestionOption multiOption2 = new QuestionOption();
        multiOption2.setOptionLabel("B");
        multiOption2.setOptionContent("String");
        multiOption2.setIsCorrect(false);
        multiOption2.setSortOrder(2);
        multiOption2.setQuestion(multipleChoiceQuestion);
        
        QuestionOption multiOption3 = new QuestionOption();
        multiOption3.setOptionLabel("C");
        multiOption3.setOptionContent("boolean");
        multiOption3.setIsCorrect(true);
        multiOption3.setSortOrder(3);
        multiOption3.setQuestion(multipleChoiceQuestion);
        
        QuestionOption multiOption4 = new QuestionOption();
        multiOption4.setOptionLabel("D");
        multiOption4.setOptionContent("double");
        multiOption4.setIsCorrect(true);
        multiOption4.setSortOrder(4);
        multiOption4.setQuestion(multipleChoiceQuestion);
        
        multipleChoiceQuestion.setOptions(Arrays.asList(multiOption1, multiOption2, multiOption3, multiOption4));
        
        // 保存题目
        questionRepository.save(singleChoiceQuestion);
        questionRepository.save(multipleChoiceQuestion);
        
        log.info("已创建 {} 道示例题目", 2);
    }
    
    /**
     * 创建示例记账数据（可选）
     */
    private void createSampleExpenses() {
        // 获取分类实体
        Category foodCategory = categoryRepository.findByName("餐饮").orElse(null);
        Category transportCategory = categoryRepository.findByName("交通").orElse(null);
        Category shoppingCategory = categoryRepository.findByName("购物").orElse(null);
        
        if (foodCategory == null || transportCategory == null || shoppingCategory == null) {
            log.warn("部分分类不存在，跳过示例记账数据创建");
            return;
        }
        
        // 创建一些示例记账记录
        Expense expense1 = new Expense();
        expense1.setAmount(new BigDecimal("50.00"));
        expense1.setCategory(foodCategory);
        expense1.setCategoryName(foodCategory.getName());
        expense1.setDescription("午餐");
        expense1.setExpenseDate(LocalDate.now().minusDays(1));
        expense1.setPayer("张三");
        expense1.setIsPublicExpense(false);
        
        Expense expense2 = new Expense();
        expense2.setAmount(new BigDecimal("120.00"));
        expense2.setCategory(transportCategory);
        expense2.setCategoryName(transportCategory.getName());
        expense2.setDescription("打车费用");
        expense2.setExpenseDate(LocalDate.now());
        expense2.setPayer("李四");
        expense2.setIsPublicExpense(true);
        
        Expense expense3 = new Expense();
        expense3.setAmount(new BigDecimal("200.00"));
        expense3.setCategory(shoppingCategory);
        expense3.setCategoryName(shoppingCategory.getName());
        expense3.setDescription("日用品采购");
        expense3.setExpenseDate(LocalDate.now().minusDays(2));
        expense3.setPayer("王五");
        expense3.setIsPublicExpense(false);
        
        expenseRepository.save(expense1);
        expenseRepository.save(expense2);
        expenseRepository.save(expense3);
        
        log.info("已创建 {} 条示例记账记录", 3);
    }
    
    /**
     * 初始化用户数据
     */
    private void initializeUsers() {
        long userCount = userRepository.count();
        log.info("当前用户数量: {}", userCount);
        
        if (userCount == 0) {
            log.info("检测到用户表为空，开始初始化用户数据...");
            createSampleUsers();
            log.info("用户数据初始化完成！");
        } else {
            log.info("用户数据已存在，跳过初始化");
        }
    }
    
    /**
     * 创建示例用户数据
     */
    private void createSampleUsers() {
        // 创建用户：苏苏
        User user1 = new User();
        user1.setUsername("苏苏");
        user1.setPhoneNumber("13800138001");
        
        // 创建用户：飞飞
        User user2 = new User();
        user2.setUsername("飞飞");
        user2.setPhoneNumber("13800138002");
        
        userRepository.save(user1);
        userRepository.save(user2);
        
        log.info("创建了2个示例用户：苏苏、飞飞");
    }
    
    /**
     * 初始化分类数据
     */
    private void initializeCategories() {
        long categoryCount = categoryRepository.count();
        log.info("当前分类数量: {}", categoryCount);
        
        if (categoryCount == 0) {
            log.info("检测到分类表为空，开始初始化示例分类数据...");
            createSampleCategories();
        } else {
            log.info("检测到现有分类数据 {} 条，跳过分类数据初始化", categoryCount);
        }
    }
    
    /**
     * 创建示例分类数据
     */
    private void createSampleCategories() {
        // 创建餐饮分类
        Category category1 = new Category();
        category1.setName("餐饮");
        category1.setIcon("Food");
        category1.setDescription("餐厅用餐、外卖、零食等饮食相关支出");
        
        // 创建交通分类
        Category category2 = new Category();
        category2.setName("交通");
        category2.setIcon("Van");
        category2.setDescription("打车、公交、地铁、加油等交通出行费用");
        
        // 创建购物分类
        Category category3 = new Category();
        category3.setName("购物");
        category3.setIcon("ShoppingBag");
        category3.setDescription("日用品、服装、电子产品等购物消费");
        
        // 创建娱乐分类
        Category category4 = new Category();
        category4.setName("娱乐");
        category4.setIcon("VideoPlay");
        category4.setDescription("电影、游戏、KTV等娱乐休闲活动");
        
        // 创建医疗分类
        Category category5 = new Category();
        category5.setName("医疗");
        category5.setIcon("FirstAidKit");
        category5.setDescription("看病、买药、体检等医疗健康支出");
        
        // 创建教育分类
        Category category6 = new Category();
        category6.setName("教育");
        category6.setIcon("Reading");
        category6.setDescription("培训、书籍、课程等教育学习费用");
        
        // 创建住房分类
        Category category7 = new Category();
        category7.setName("住房");
        category7.setIcon("House");
        category7.setDescription("房租、水电费、物业费等住房相关支出");
        
        // 创建通讯分类
        Category category8 = new Category();
        category8.setName("通讯");
        category8.setIcon("Iphone");
        category8.setDescription("手机费、网费、话费等通讯费用");
        
        // 创建礼品分类
        Category category9 = new Category();
        category9.setName("礼品");
        category9.setIcon("Present");
        category9.setDescription("送礼、红包、节日礼品等支出");
        
        // 创建其他分类
        Category category10 = new Category();
        category10.setName("其他");
        category10.setIcon("More");
        category10.setDescription("其他未分类的支出");
        
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
        categoryRepository.save(category4);
        categoryRepository.save(category5);
        categoryRepository.save(category6);
        categoryRepository.save(category7);
        categoryRepository.save(category8);
        categoryRepository.save(category9);
        categoryRepository.save(category10);
        
        log.info("已创建 {} 个示例分类", 10);
    }
}