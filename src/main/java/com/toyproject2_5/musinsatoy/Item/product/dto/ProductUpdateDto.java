package com.toyproject2_5.musinsatoy.Item.product.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductUpdateDto {

    private String productId;

    @Size(min =3 , max =25, message = "3글자 이상, 25자 미만 이어야 합니다.")
    @NotNull(message = "3글자 이상, 25자 미만 이어야 합니다.")
    private String name;

    private String categoryId;

    private String productDescriptionId;

    private String registerManager;

    private String repImg;

    private String brandId;

    @Positive(message = "가격을 다시 설정해 주세요")
    private Integer price;

    private String productStatus;

}
