package com.toyproject2_5.musinsatoy.Item.productDescription.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductDescriptionDto {
    //상세 설명 더 추가할 지 생각.

    @NotBlank
    @Size(min = 10, max = 25)
    private String productDescriptionId;

    @NotBlank
    @Size(min = 10, max = 25)
    private String description;

}
