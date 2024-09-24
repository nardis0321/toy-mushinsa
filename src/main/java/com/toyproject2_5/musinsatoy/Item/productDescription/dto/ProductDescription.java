package com.toyproject2_5.musinsatoy.Item.productDescription.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDescription {

    private String productDescriptionId;

    private String description;

    private LocalDateTime modifyDatetime;



}
