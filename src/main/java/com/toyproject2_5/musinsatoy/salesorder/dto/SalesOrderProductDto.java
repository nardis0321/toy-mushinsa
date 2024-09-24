package com.toyproject2_5.musinsatoy.salesorder.dto;

import java.util.List;


public record SalesOrderProductDto(
        String orderId,
        String productId,
        int price,
        int amount
        ,List<SalesOrderProductOptionDto> options
        ) {
}
