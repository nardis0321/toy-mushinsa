package com.toyproject2_5.musinsatoy.Item.product.dto.stock;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StockDto {

    private String productOptionId;
    private String productId;
    private int quantity;
    private String saleState;

}
