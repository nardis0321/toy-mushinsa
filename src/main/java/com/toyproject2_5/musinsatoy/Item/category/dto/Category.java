package com.toyproject2_5.musinsatoy.Item.category.dto;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Category {

    public static String FIRSTCATEGORY = "C01";

    private String categoryId;
    private String categoryName;
    private String parentCategoryId;


}
