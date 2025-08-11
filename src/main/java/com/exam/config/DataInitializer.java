package com.exam.config;

import com.exam.entity.Question;
import com.exam.entity.QuestionOption;
import com.exam.enums.Difficulty;
import com.exam.enums.QuestionStatus;
import com.exam.enums.QuestionType;
import com.exam.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 数据初始化器 - 用于插入示例数据
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final QuestionRepository questionRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // 检查是否已有数据
        if (questionRepository.count() > 0) {
            return;
        }
        
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
        
        System.out.println("示例数据初始化完成！");
    }
}