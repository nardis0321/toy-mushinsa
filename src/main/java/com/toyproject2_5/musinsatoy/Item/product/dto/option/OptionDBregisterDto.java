package com.toyproject2_5.musinsatoy.Item.product.dto.option;


import com.toyproject2_5.musinsatoy.Item.product.dto.stock.StockRegisterDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class OptionDBregisterDto {
    String productId;
    String optionId;
    String optionName;
    String optionCategory;
    int optionDepth;

    public OptionDBregisterDto(String productId, String optionId, String optionName, int optionDepth, String optionCategory){
        this.optionId = optionId;
        this.optionName = optionName;
        this.optionDepth = optionDepth;
        this.productId = productId;
        this.optionCategory = optionCategory;
    }
}
