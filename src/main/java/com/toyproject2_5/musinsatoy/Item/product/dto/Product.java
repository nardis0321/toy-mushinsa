package com.toyproject2_5.musinsatoy.Item.product.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class Product {



    public static String DEFAULT_STATUS = "SELL";
    public static int DEFAULT_NUM = 0;

    // 제품 상태를 isDisplayed가 아니라 enum으로 상태를 나타낼지 생각.

    private String productId;


    private String brandProductCode;

    //fk
    private String productDescriptionId;

    //fk
    private String categoryId;

    //fk
    private String brandId;

    private String name;

    private String repImg;

    private int price;

    private int productOptionDepth;

    private int eventPrice;

    private String productStatus;

    private LocalDateTime createDatetime;

    private String modifyDatetime;

    private long salesQuantity;

    private String registerManager;

    private float starRating;

    private int viewCount;

    private int reviewCount;


}