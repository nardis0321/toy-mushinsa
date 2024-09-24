package com.toyproject2_5.musinsatoy.salesorder.entity;

import lombok.*;

import java.util.List;

@Setter @Getter
@NoArgsConstructor
@ToString
public class SalesOrderProduct {
    private String orderId;
    private String productId;
    private int price;
    private int amount;
    private List<SalesOrderProductOption> options;
}
