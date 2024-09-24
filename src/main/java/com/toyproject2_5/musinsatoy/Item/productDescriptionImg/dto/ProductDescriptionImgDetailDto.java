package com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductDescriptionImgDetailDto {

    //정렬 순서는 sql에서 정렬하고 오기.

    private String name;

    private String path;

    private String kindOf;

}
