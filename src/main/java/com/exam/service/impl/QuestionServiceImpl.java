package com.exam.service.impl;

import com.exam.common.PageResult;
import com.exam.dto.QuestionDTO;
import com.exam.dto.QuestionOptionDTO;
import com.exam.dto.QuestionQueryDTO;
import com.exam.dto.QuestionExamDTO;
import com.exam.dto.QuestionOptionExamDTO;
import com.exam.dto.QuestionAnswerDTO;
import com.exam.entity.Question;
import com.exam.entity.QuestionOption;
import com.exam.enums.Difficulty;
import com.exam.enums.QuestionStatus;
import com.exam.enums.QuestionType;
import com.exam.repository.QuestionRepository;
import com.exam.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 题目服务实现类
 */
// @Service
@RequiredArgsConstructor
@Transactional
public class QuestionServiceImpl implements QuestionService {
    
    private final QuestionRepository questionRepository;
    
    @Override
    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        Question question = convertToEntity(questionDTO);
        Question savedQuestion = questionRepository.save(question);
        return convertToDTO(savedQuestion);
    }
    
    @Override
    public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO) {
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("题目不存在，ID: " + id));
        
        // 更新基本信息
        existingQuestion.setTitle(questionDTO.getTitle());
        existingQuestion.setContent(questionDTO.getContent());
        existingQuestion.setType(questionDTO.getType());
        existingQuestion.setDifficulty(questionDTO.getDifficulty());
        existingQuestion.setCategory(questionDTO.getCategory());
        existingQuestion.setScore(questionDTO.getScore());
        existingQuestion.setExplanation(questionDTO.getExplanation());
        existingQuestion.setStatus(questionDTO.getStatus());
        
        // 更新选项
        existingQuestion.getOptions().clear();
        if (questionDTO.getOptions() != null) {
            List<QuestionOption> options = questionDTO.getOptions().stream()
                    .map(optionDTO -> {
                        QuestionOption option = new QuestionOption();
                        BeanUtils.copyProperties(optionDTO, option);
                        option.setQuestion(existingQuestion);
                        return option;
                    })
                    .collect(Collectors.toList());
            existingQuestion.getOptions().addAll(options);
        }
        
        Question savedQuestion = questionRepository.save(existingQuestion);
        return convertToDTO(savedQuestion);
    }
    
    @Override
    @Transactional(readOnly = true)
    public QuestionDTO getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("题目不存在，ID: " + id));
        return convertToDTO(question);
    }
    
    @Override
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new RuntimeException("题目不存在，ID: " + id);
        }
        questionRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PageResult<QuestionDTO> getQuestions(QuestionQueryDTO queryDTO) {
        // 构建排序
        Sort.Direction direction = "desc".equalsIgnoreCase(queryDTO.getSortDirection()) 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, queryDTO.getSortBy());
        
        // 构建分页
        Pageable pageable = PageRequest.of(queryDTO.getPage(), queryDTO.getSize(), sort);
        
        // 执行查询
        Page<Question> questionPage = questionRepository.findQuestionsWithFilters(
                queryDTO.getType(),
                queryDTO.getDifficulty(),
                queryDTO.getCategory(),
                queryDTO.getStatus(),
                queryDTO.getKeyword(),
                pageable
        );
        
        // 转换为DTO
        Page<QuestionDTO> dtoPage = questionPage.map(this::convertToDTO);
        return PageResult.of(dtoPage);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<QuestionDTO> getQuestionsByType(QuestionType type) {
        List<Question> questions = questionRepository.findByType(type);
        return questions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<QuestionDTO> getQuestionsByCategory(String category) {
        List<Question> questions = questionRepository.findByCategory(category);
        return questions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<QuestionDTO> getQuestionsByDifficulty(Difficulty difficulty) {
        List<Question> questions = questionRepository.findByDifficulty(difficulty);
        return questions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<QuestionDTO> getQuestionsByStatus(QuestionStatus status) {
        List<Question> questions = questionRepository.findByStatus(status);
        return questions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<QuestionDTO> searchQuestions(String keyword) {
        List<Question> questions = questionRepository.findByTitleContaining(keyword);
        return questions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getQuestionStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 总题目数
        long totalQuestions = questionRepository.count();
        statistics.put("totalQuestions", totalQuestions);
        
        // 按类型统计
        List<Object[]> typeStats = questionRepository.countByTypeAndStatus(QuestionStatus.ACTIVE);
        Map<String, Long> typeCount = new HashMap<>();
        for (Object[] stat : typeStats) {
            typeCount.put(stat[0].toString(), (Long) stat[1]);
        }
        statistics.put("typeStatistics", typeCount);
        
        // 按难度统计
        List<Object[]> difficultyStats = questionRepository.countByDifficultyAndStatus(QuestionStatus.ACTIVE);
        Map<String, Long> difficultyCount = new HashMap<>();
        for (Object[] stat : difficultyStats) {
            difficultyCount.put(stat[0].toString(), (Long) stat[1]);
        }
        statistics.put("difficultyStatistics", difficultyCount);
        
        return statistics;
    }
    
    @Override
    public void deleteQuestions(List<Long> ids) {
        questionRepository.deleteAllById(ids);
    }
    
    @Override
    public void updateQuestionStatus(List<Long> ids, QuestionStatus status) {
        List<Question> questions = questionRepository.findAllById(ids);
        questions.forEach(question -> question.setStatus(status));
        questionRepository.saveAll(questions);
    }
    
    /**
     * 实体转DTO
     */
    private QuestionDTO convertToDTO(Question question) {
        QuestionDTO dto = new QuestionDTO();
        BeanUtils.copyProperties(question, dto);
        
        if (question.getOptions() != null) {
            List<QuestionOptionDTO> optionDTOs = question.getOptions().stream()
                    .map(option -> {
                        QuestionOptionDTO optionDTO = new QuestionOptionDTO();
                        BeanUtils.copyProperties(option, optionDTO);
                        return optionDTO;
                    })
                    .collect(Collectors.toList());
            dto.setOptions(optionDTOs);
        }
        
        return dto;
    }
    
    /**
     * DTO转实体
     */
    private Question convertToEntity(QuestionDTO dto) {
        Question question = new Question();
        BeanUtils.copyProperties(dto, question, "id", "options");
        
        if (dto.getOptions() != null) {
            List<QuestionOption> options = dto.getOptions().stream()
                    .map(optionDTO -> {
                        QuestionOption option = new QuestionOption();
                        BeanUtils.copyProperties(optionDTO, option, "id");
                        option.setQuestion(question);
                        return option;
                    })
                    .collect(Collectors.toList());
            question.setOptions(options);
        }
        
        return question;
    }
    
    @Override
    @Transactional(readOnly = true)
    public QuestionExamDTO getRandomExamQuestion(QuestionType type, Difficulty difficulty, String category) {
        // 构建查询条件
        List<Question> questions = questionRepository.findAll().stream()
                .filter(q -> q.getStatus() == QuestionStatus.ACTIVE)
                .filter(q -> type == null || q.getType() == type)
                .filter(q -> difficulty == null || q.getDifficulty() == difficulty)
                .filter(q -> category == null || category.equals(q.getCategory()))
                .collect(Collectors.toList());
        
        if (questions.isEmpty()) {
            return null;
        }
        
        // 随机选择一道题目
        Random random = new Random();
        Question randomQuestion = questions.get(random.nextInt(questions.size()));
        
        return convertToExamDTO(randomQuestion);
    }
    
    @Override
    @Transactional(readOnly = true)
    public QuestionAnswerDTO getQuestionAnswer(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("题目不存在，ID: " + questionId));
        
        if (question.getStatus() != QuestionStatus.ACTIVE) {
            throw new RuntimeException("题目已下线，无法获取答案");
        }
        
        QuestionAnswerDTO answerDTO = new QuestionAnswerDTO();
        answerDTO.setQuestionId(questionId);
        answerDTO.setExplanation(question.getExplanation());
        
        // 获取正确答案选项
        if (question.getOptions() != null) {
            List<QuestionOptionDTO> correctOptions = question.getOptions().stream()
                    .filter(QuestionOption::getIsCorrect)
                    .map(option -> {
                        QuestionOptionDTO optionDTO = new QuestionOptionDTO();
                        BeanUtils.copyProperties(option, optionDTO);
                        return optionDTO;
                    })
                    .collect(Collectors.toList());
            answerDTO.setCorrectOptions(correctOptions);
        }
        
        return answerDTO;
    }
    
    /**
     * 实体转考试DTO（不包含答案和解析）
     */
    private QuestionExamDTO convertToExamDTO(Question question) {
        QuestionExamDTO dto = new QuestionExamDTO();
        dto.setId(question.getId());
        dto.setTitle(question.getTitle());
        dto.setContent(question.getContent());
        dto.setType(question.getType());
        dto.setDifficulty(question.getDifficulty());
        dto.setCategory(question.getCategory());
        dto.setScore(question.getScore());
        
        if (question.getOptions() != null) {
            List<QuestionOptionExamDTO> optionDTOs = question.getOptions().stream()
                    .map(option -> {
                        QuestionOptionExamDTO optionDTO = new QuestionOptionExamDTO();
                        optionDTO.setId(option.getId());
                        optionDTO.setOptionKey(option.getOptionLabel());
                        optionDTO.setOptionValue(option.getOptionContent());
                        // 不设置isCorrect字段
                        return optionDTO;
                    })
                    .collect(Collectors.toList());
            dto.setOptions(optionDTOs);
        }
        
        return dto;
    }
}