package com.exam.controller;

import com.exam.common.PageResult;
import com.exam.common.Result;
import com.exam.dto.QuestionDTO;
import com.exam.dto.QuestionQueryDTO;
import com.exam.enums.Difficulty;
import com.exam.enums.QuestionStatus;
import com.exam.enums.QuestionType;
import com.exam.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 后台题目管理控制器
 * 用于管理员对题目进行增删改查等管理操作
 */
// @RestController
@RequestMapping("/api/admin/questions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminQuestionController {
    
    private final QuestionService questionService;
    
    /**
     * 创建题目
     */
    @PostMapping
    public Result<QuestionDTO> createQuestion(@RequestBody QuestionDTO questionDTO) {
        try {
            QuestionDTO createdQuestion = questionService.createQuestion(questionDTO);
            return Result.success("题目创建成功", createdQuestion);
        } catch (Exception e) {
            return Result.error("题目创建失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新题目
     */
    @PutMapping("/{id}")
    public Result<QuestionDTO> updateQuestion(@PathVariable Long id, @RequestBody QuestionDTO questionDTO) {
        try {
            QuestionDTO updatedQuestion = questionService.updateQuestion(id, questionDTO);
            return Result.success("题目更新成功", updatedQuestion);
        } catch (Exception e) {
            return Result.error("题目更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取题目详情（包含所有信息）
     */
    @GetMapping("/{id}")
    public Result<QuestionDTO> getQuestionById(@PathVariable Long id) {
        try {
            QuestionDTO question = questionService.getQuestionById(id);
            return Result.success("获取题目成功", question);
        } catch (Exception e) {
            return Result.error("获取题目失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除题目
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteQuestion(@PathVariable Long id) {
        try {
            questionService.deleteQuestion(id);
            return Result.success("题目删除成功", null);
        } catch (Exception e) {
            return Result.error("题目删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 分页查询题目（支持多条件筛选）
     */
    @GetMapping
    public Result<PageResult<QuestionDTO>> getQuestions(
            @RequestParam(required = false) QuestionType type,
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) QuestionStatus status,
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
            queryDTO.setStatus(status);
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
     * 根据类型获取题目
     */
    @GetMapping("/type/{type}")
    public Result<List<QuestionDTO>> getQuestionsByType(@PathVariable QuestionType type) {
        try {
            List<QuestionDTO> questions = questionService.getQuestionsByType(type);
            return Result.success("获取题目成功", questions);
        } catch (Exception e) {
            return Result.error("获取题目失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据分类获取题目
     */
    @GetMapping("/category/{category}")
    public Result<List<QuestionDTO>> getQuestionsByCategory(@PathVariable String category) {
        try {
            List<QuestionDTO> questions = questionService.getQuestionsByCategory(category);
            return Result.success("获取题目成功", questions);
        } catch (Exception e) {
            return Result.error("获取题目失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据难度获取题目
     */
    @GetMapping("/difficulty/{difficulty}")
    public Result<List<QuestionDTO>> getQuestionsByDifficulty(@PathVariable Difficulty difficulty) {
        try {
            List<QuestionDTO> questions = questionService.getQuestionsByDifficulty(difficulty);
            return Result.success("获取题目成功", questions);
        } catch (Exception e) {
            return Result.error("获取题目失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据状态获取题目
     */
    @GetMapping("/status/{status}")
    public Result<List<QuestionDTO>> getQuestionsByStatus(@PathVariable QuestionStatus status) {
        try {
            List<QuestionDTO> questions = questionService.getQuestionsByStatus(status);
            return Result.success("获取题目成功", questions);
        } catch (Exception e) {
            return Result.error("获取题目失败: " + e.getMessage());
        }
    }
    
    /**
     * 搜索题目
     */
    @GetMapping("/search")
    public Result<List<QuestionDTO>> searchQuestions(@RequestParam String keyword) {
        try {
            List<QuestionDTO> questions = questionService.searchQuestions(keyword);
            return Result.success("搜索题目成功", questions);
        } catch (Exception e) {
            return Result.error("搜索题目失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取题目统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getQuestionStatistics() {
        try {
            Map<String, Object> statistics = questionService.getQuestionStatistics();
            return Result.success("获取统计信息成功", statistics);
        } catch (Exception e) {
            return Result.error("获取统计信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量删除题目
     */
    @DeleteMapping("/batch")
    public Result<Void> deleteQuestions(@RequestBody List<Long> ids) {
        try {
            questionService.deleteQuestions(ids);
            return Result.success("批量删除题目成功", null);
        } catch (Exception e) {
            return Result.error("批量删除题目失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量更新题目状态
     */
    @PutMapping("/batch/status")
    public Result<Void> updateQuestionStatus(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) request.get("ids");
            QuestionStatus status = QuestionStatus.valueOf((String) request.get("status"));
            questionService.updateQuestionStatus(ids, status);
            return Result.success("批量更新题目状态成功", null);
        } catch (Exception e) {
            return Result.error("批量更新题目状态失败: " + e.getMessage());
        }
    }
}