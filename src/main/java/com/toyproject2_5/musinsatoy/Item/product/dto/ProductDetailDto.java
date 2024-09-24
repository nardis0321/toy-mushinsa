package com.toyproject2_5.musinsatoy.Item.product.dto;

import com.toyproject2_5.musinsatoy.Item.category.dto.CategoryHierarchyDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.option.OptionStockDetailDto;
import com.toyproject2_5.musinsatoy.Item.productDescription.dto.ProductDescriptionDto;
import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto.ProductDescriptionImgDetailDto;
import lombok.*;

import javax.swing.text.html.Option;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDetailDto {


    private String productId;
    private String Name;
    private String brandId;
    private String brandName;
    private int price;
    private int eventPrice;
    private ProductDescriptionDto productDescription;
    private String repImg;
    private String categoryId;
    private String categoryName;
    //자신의 카테고리 이름은 parentCategorys에 포함 되어 있음.
    private String productStatus;


    private List<CategoryHierarchyDto> parentCategorys;
    //    private String productStatus;
    private int salesQuantity;

    private String registerManager;

    //url만 받아와도 되는지?
    private List<ProductDescriptionImgDetailDto> productImages;
    private List<ProductDescriptionImgDetailDto> descriptionImages;


    private int productOptionDepth;
    private List<OptionStockDetailDto> optionList;

    //
//    private List<> lastOptionList;

    //review_content

    private int likeCount;
    private int starRating;
    private int reviewCount;
    private int viewCount;




}
