package com.toyproject2_5.musinsatoy.salesorder.dto;

public record SalesOrderProductOptionDto(
        String optionPk,    // auto
        String orderId,
        String productId,
        String optionId,
        String optionCategory, // 사이즈, 컬러
        String optionName, // s,m red,blue
        String optionDepth
) {
}
