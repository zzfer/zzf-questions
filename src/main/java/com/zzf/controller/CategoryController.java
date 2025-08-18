package com.zzf.controller;

import com.zzf.dto.CategoryDTO;
import com.zzf.entity.Category;
import com.zzf.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * 获取所有分类
     */
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryRepository.findAllOrderByName();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }

    /**
     * 根据ID获取分类
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            return ResponseEntity.ok(convertToDTO(category.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 根据名称获取分类
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryDTO> getCategoryByName(@PathVariable String name) {
        Optional<Category> category = categoryRepository.findByName(name);
        if (category.isPresent()) {
            return ResponseEntity.ok(convertToDTO(category.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 创建新分类
     */
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        // 检查分类名称是否已存在
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setIcon(categoryDTO.getIcon());
        category.setDescription(categoryDTO.getDescription());

        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedCategory));
    }

    /**
     * 更新分类
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (!existingCategory.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // 检查分类名称是否已被其他分类使用
        Optional<Category> categoryWithSameName = categoryRepository.findByName(categoryDTO.getName());
        if (categoryWithSameName.isPresent() && !categoryWithSameName.get().getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Category category = existingCategory.get();
        category.setName(categoryDTO.getName());
        category.setIcon(categoryDTO.getIcon());
        category.setDescription(categoryDTO.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(convertToDTO(updatedCategory));
    }

    /**
     * 只更新分类图标
     */
    @PatchMapping("/{id}/icon")
    public ResponseEntity<CategoryDTO> updateCategoryIcon(@PathVariable Long id, @RequestBody Map<String, String> iconData) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (!existingCategory.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Category category = existingCategory.get();
        category.setIcon(iconData.get("icon"));

        Category updatedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(convertToDTO(updatedCategory));
    }

    /**
     * 更新分类名称和描述
     */
    @PatchMapping("/{id}/info")
    public ResponseEntity<CategoryDTO> updateCategoryInfo(@PathVariable Long id, @RequestBody Map<String, String> infoData) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (!existingCategory.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Category category = existingCategory.get();
        
        // 更新名称（如果提供）
        if (infoData.containsKey("name")) {
            category.setName(infoData.get("name"));
        }
        
        // 更新描述（如果提供）
        if (infoData.containsKey("description")) {
            category.setDescription(infoData.get("description"));
        }

        Category updatedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(convertToDTO(updatedCategory));
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 搜索分类
     */
    @GetMapping("/search")
    public ResponseEntity<List<CategoryDTO>> searchCategories(@RequestParam String name) {
        List<Category> categories = categoryRepository.findByNameContaining(name);
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }

    /**
     * 删除所有分类
     */
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllCategories() {
        categoryRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    /**
     * 将Category实体转换为CategoryDTO
     */
    private CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getIcon(),
                category.getDescription(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}