package com.toyproject2_5.musinsatoy.Item.category.dao;

import com.toyproject2_5.musinsatoy.Item.category.dto.Category;
import com.toyproject2_5.musinsatoy.Item.category.dto.CategoryHierarchyDto;
import com.toyproject2_5.musinsatoy.Item.category.dto.SubCategoryDto;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
@Repository
public class CategoryDaoMysql implements CategoryDao {
    private final SqlSession sqlSession;

    private String namespace ="categoryMapper.";

    @Override
    public int insert(Category category) throws Exception {
        return sqlSession.insert(namespace + "insertCategory", category);
    }

    @Override
    public String findById(String categoryId) throws Exception {
        return sqlSession.selectOne(namespace+"findNameById", categoryId);
    }

    @Override
    public List<CategoryHierarchyDto> findUpperCategoryHierarchyById(String categoryId) throws Exception {
        return sqlSession.selectList(namespace+"findUpperCategoryHierarchyById", categoryId);
    }

    @Override
    public List<CategoryHierarchyDto> findLowerCategoryHierarchyById(String categoryId) throws Exception {
        return sqlSession.selectList(namespace+"findLowerCategoryHierarchyById", categoryId);
    }

    @Override
    public List<SubCategoryDto> findSubCategoryById(String categoryId) throws Exception {
        return sqlSession.selectList(namespace+"findLowerCategoryById", categoryId);
    }

    @Override
    public int update(Category category) throws Exception {
        return sqlSession.update(namespace + "updateCategory", category);
    }

    @Override
    public int deleteByCategoryId(String categoryId) throws Exception {
        return sqlSession.delete(namespace + "deleteByCategoryId", categoryId);
    }
}
