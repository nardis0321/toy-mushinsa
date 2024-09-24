package com.toyproject2_5.musinsatoy.Item.category.dao;

import com.toyproject2_5.musinsatoy.Item.category.dto.Category;
import com.toyproject2_5.musinsatoy.Item.category.dto.CategoryHierarchyDto;
import com.toyproject2_5.musinsatoy.Item.category.dto.SubCategoryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryDao {

    int insert(Category category) throws Exception;

    String findById(@Param("categoryId")  String categoryId) throws Exception;
    List<CategoryHierarchyDto> findUpperCategoryHierarchyById(@Param("categoryId") String categoryId ) throws Exception;
    List<CategoryHierarchyDto> findLowerCategoryHierarchyById( @Param("categoryId") String categoryId ) throws Exception;

    List<SubCategoryDto> findSubCategoryById(@Param("categoryId") String categoryId ) throws Exception;

    int update(Category category) throws Exception;

    int deleteByCategoryId(@Param("categoryId") String categoryId) throws  Exception;



}
