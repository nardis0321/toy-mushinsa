package com.toyproject2_5.musinsatoy.Item.category.service;

import com.toyproject2_5.musinsatoy.Item.category.dao.CategoryDao;
import com.toyproject2_5.musinsatoy.Item.category.dto.SubCategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryDao categoryDao;

    public String findById(String categoryId) {
        try {
            if(categoryDao.findById(categoryId)!=null) {
                return categoryId;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //특정 카테고리 한 계층 아래의 카테고리 찾기
    public List<SubCategoryDto> findSubCategory(String categoryId) throws Exception {

        List<SubCategoryDto> categoryList =categoryDao.findSubCategoryById(categoryId);

        return categoryList;
    }

    // Keyword 검색 화면에 출력될 상품 카테고리.
    // 스크롤 많이할 수 있게 범위 크게하기.-> 스크롤 확인.
    public void secondAllCategory() throws Exception {
    //select * from category c1 Right JOIN (select * from category WHERE parent_category_id ='C01') c2 ON c1.parent_category_id=c2.category_id;

    }

}
