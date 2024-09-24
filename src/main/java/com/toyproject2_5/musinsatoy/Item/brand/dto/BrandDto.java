package com.toyproject2_5.musinsatoy.Item.brand.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BrandDto {
    private String brandId;
    private String name;
    private String img;
    private LocalDateTime createDatetime;
    private LocalDateTime modifyDatetime;
}
