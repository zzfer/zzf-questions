package com.exam.controller;

import com.exam.common.PageResult;
import com.exam.common.Result;
import com.exam.dto.QuestionDTO;
import com.exam.dto.QuestionQueryDTO;
import com.exam.dto.QuestionExamDTO;
import com.exam.dto.QuestionAnswerDTO;
import com.exam.enums.Difficulty;
import com.exam.enums.QuestionStatus;
import com.exam.enums.QuestionType;
import com.exam.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 前台题目展示控制器
 * 用于前台用户查看和获取题目信息（只读操作）
 */
@RestController
@RequestMapping("/api/public/questions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublicQuestionController {
    
    private final QuestionService questionService;
    
    /**
     * 根据ID获取题目详情（仅返回激活状态的题目）
     */
    @GetMapping("/{id}")
    public Result<QuestionDTO> getQuestionById(@PathVariable Long id) {
        try {
            QuestionDTO question = questionService.getQuestionById(id);
            // 只返回激活状态的题目
            if (question != null && question.getStatus() == QuestionStatus.ACTIVE) {
                return Result.success("获取题目成功", question);
            } else {
                return Result.notFound("题目不存在或已下线");
            }
        } catch (Exception e) {
            return Result.error("获取题目失败: " + e.getMessage());
        }
    }
    
    /**
     * 分页查询激活状态的题目
     */
    @GetMapping
    public Result<PageResult<QuestionDTO>> getActiveQuestions(
            @RequestParam(required = false) QuestionType type,
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            QuestionQueryDTO queryDTO = new QuestionQueryDTO();
            queryDTO.setType(type);
            queryDTO.setDifficulty(difficulty);
            queryDTO.setCategory(category);
            queryDTO.setStatus(QuestionStatus.ACTIVE); // 只查询激活状态的题目
            queryDTO.setKeyword(keyword);
            queryDTO.setPage(page);
            queryDTO.setSize(size);
            queryDTO.setSortBy(sortBy);
            queryDTO.setSortDirection(sortDirection);
            
            PageResult<QuestionDTO> result = questionService.getQuestions(queryDTO);
            return Result.success("获取题目列表成功", result);
        } catch (Exception e) {
            return Result.error("获取题目列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据类型获取激活状态的题目
     */
    @GetMapping("/type/{type}")
    public Result<List<QuestionDTO>> getActiveQuestionsByType(@PathVariable QuestionType type) {
        try {
            List<QuestionDTO> questions = questionService.getQuestionsByType(type);
            // 过滤只返回激活状态的题目
            List<QuestionDTO> activeQuestions = questions.stream()
                    .filter(q -> q.getStatus() == QuestionStatus.ACTIVE)
                    .toList();
            return Result.success("获取题目成功", activeQuestions);
        } catch (Exception e) {
            return Result.error("获取题目失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据分类获取激活状态的题目
     */
    @GetMapping("/category/{category}")
    public Result<List<QuestionDTO>> getActiveQuestionsByCategory(@PathVariable String category) {
        try {
            List<QuestionDTO> questions = questionService.getQuestionsByCategory(category);
            // 过滤只返回激活状态的题目
            List<QuestionDTO> activeQuestions = questions.stream()
                    .filter(q -> q.getStatus() == QuestionStatus.ACTIVE)
                    .toList();
            return Result.success("获取题目成功", activeQuestions);
        } catch (Exception e) {
            return Result.error("获取题目失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据难度获取激活状态的题目
     */
    @GetMapping("/difficulty/{difficulty}")
    public Result<List<QuestionDTO>> getActiveQuestionsByDifficulty(@PathVariable Difficulty difficulty) {
        try {
            List<QuestionDTO> questions = questionService.getQuestionsByDifficulty(difficulty);
            // 过滤只返回激活状态的题目
            List<QuestionDTO> activeQuestions = questions.stream()
                    .filter(q -> q.getStatus() == QuestionStatus.ACTIVE)
                    .toList();
            return Result.success("获取题目成功", activeQuestions);
        } catch (Exception e) {
            return Result.error("获取题目失败: " + e.getMessage());
        }
    }
    
    /**
     * 搜索激活状态的题目
     */
    @GetMapping("/search")
    public Result<List<QuestionDTO>> searchActiveQuestions(@RequestParam String keyword) {
        try {
            List<QuestionDTO> questions = questionService.searchQuestions(keyword);
            // 过滤只返回激活状态的题目
            List<QuestionDTO> activeQuestions = questions.stream()
                    .filter(q -> q.getStatus() == QuestionStatus.ACTIVE)
                    .toList();
            return Result.success("搜索题目成功", activeQuestions);
        } catch (Exception e) {
            return Result.error("搜索题目失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取激活题目的统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getActiveQuestionStatistics() {
        try {
            Map<String, Object> statistics = questionService.getQuestionStatistics();
            return Result.success("获取统计信息成功", statistics);
        } catch (Exception e) {
            return Result.error("获取统计信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 随机获取指定数量的题目（用于考试）
     */
    @GetMapping("/random")
    public Result<List<QuestionDTO>> getRandomQuestions(
            @RequestParam(defaultValue = "10") Integer count,
            @RequestParam(required = false) QuestionType type,
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(required = false) String category) {
        try {
            // 构建查询条件
            QuestionQueryDTO queryDTO = new QuestionQueryDTO();
            queryDTO.setType(type);
            queryDTO.setDifficulty(difficulty);
            queryDTO.setCategory(category);
            queryDTO.setStatus(QuestionStatus.ACTIVE);
            queryDTO.setPage(0);
            queryDTO.setSize(count * 3); // 获取更多数据用于随机选择
            
            PageResult<QuestionDTO> result = questionService.getQuestions(queryDTO);
            List<QuestionDTO> questions = result.getContent();
            
            // 随机选择指定数量的题目
            if (questions.size() <= count) {
                return Result.success("获取随机题目成功", questions);
            } else {
                List<QuestionDTO> randomQuestions = questions.stream()
                        .limit(count)
                        .toList();
                return Result.success("获取随机题目成功", randomQuestions);
            }
        } catch (Exception e) {
            return Result.error("获取随机题目失败: " + e.getMessage());
        }
    }
    
    /**
     * 随机获取一道考试题目（不包含答案和解析）
     */
    @GetMapping("/exam/random")
    public Result<QuestionExamDTO> getRandomExamQuestion(
            @RequestParam(required = false) QuestionType type,
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(required = false) String category) {
        try {
            QuestionExamDTO question = questionService.getRandomExamQuestion(type, difficulty, category);
            if (question != null) {
                return Result.success("获取考试题目成功", question);
            } else {
                return Result.notFound("没有找到符合条件的题目");
            }
        } catch (Exception e) {
            return Result.error("获取考试题目失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取题目的答案和解析
     */
    @GetMapping("/{id}/answer")
    public Result<QuestionAnswerDTO> getQuestionAnswer(@PathVariable Long id) {
        try {
            QuestionAnswerDTO answer = questionService.getQuestionAnswer(id);
            return Result.success("获取答案解析成功", answer);
        } catch (Exception e) {
            return Result.error("获取答案解析失败: " + e.getMessage());
        }
    }
}