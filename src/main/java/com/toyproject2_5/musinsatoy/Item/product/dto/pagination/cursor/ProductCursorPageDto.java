package com.toyproject2_5.musinsatoy.Item.product.dto.pagination.cursor;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
//extends ProductPageDto 하려고 했는데 맵핑이 자동으로 안됨...
public class ProductCursorPageDto {

    private String productId;
    private String productName;
    private int price;
    private String repImg;
    private int starRating;
    private String brandId;
    private String brandName;
    private int salesQuantity;
}
