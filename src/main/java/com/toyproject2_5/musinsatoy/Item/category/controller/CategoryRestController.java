package com.toyproject2_5.musinsatoy.Item.category.controller;

import com.toyproject2_5.musinsatoy.Item.category.dto.SubCategoryDto;
import com.toyproject2_5.musinsatoy.Item.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    /*
    * 특정 카테고리의 한 계층 아래 카테고리 전부 찾기.
    *  - 상품 등록시
    *
    * */
    @GetMapping("/{categoryId}/subcategory")
    public ResponseEntity<?> getSubcategories(@PathVariable String categoryId) throws Exception {

        List<SubCategoryDto>  categoryList = categoryService.findSubCategory(categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(categoryList);
    }


}
