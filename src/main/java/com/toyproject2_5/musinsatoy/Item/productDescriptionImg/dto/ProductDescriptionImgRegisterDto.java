package com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductDescriptionImgRegisterDto {

    private String fileName;
    private Long fileSize;
    private String fileType;

}
