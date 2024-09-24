package com.toyproject2_5.musinsatoy.salesorder.entity;

import lombok.*;

@Setter @Getter
@NoArgsConstructor
@ToString
public class SalesOrderProductOption {
    private String optionPk;
    private String orderId;
    private String productId;
    private String optionId;
    private String optionCategory;
    private String optionName;
    private String optionDepth;
}
