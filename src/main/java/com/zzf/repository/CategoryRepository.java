package com.zzf.repository;

import com.zzf.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * 根据分类名称查找分类
     */
    Optional<Category> findByName(String name);
    
    /**
     * 检查分类名称是否存在
     */
    boolean existsByName(String name);
    
    /**
     * 根据分类名称模糊查询
     */
    @Query("SELECT c FROM Category c WHERE c.name LIKE %:name%")
    List<Category> findByNameContaining(@Param("name") String name);
    
    /**
     * 获取所有分类，按创建时间排序
     */
    @Query("SELECT c FROM Category c ORDER BY c.createdAt ASC")
    List<Category> findAllOrderByCreatedAt();
    
    /**
     * 获取所有分类，按排序字段排序
     */
    @Query("SELECT c FROM Category c ORDER BY c.sortOrder ASC, c.name ASC")
    List<Category> findAllOrderByName();
    
    /**
     * 根据图标查找分类
     */
    List<Category> findByIcon(String icon);
}