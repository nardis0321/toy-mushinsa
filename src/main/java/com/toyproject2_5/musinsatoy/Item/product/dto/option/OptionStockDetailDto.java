package com.toyproject2_5.musinsatoy.Item.product.dto.option;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OptionStockDetailDto {
    private String productOptionId;
    private String optionName;
    private String optionCategory;
    private int optionDepth;
    private int quantity;
    private String saleState;
}
