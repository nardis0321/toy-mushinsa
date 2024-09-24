package com.toyproject2_5.musinsatoy.salesorder.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter @Getter
@NoArgsConstructor
@ToString
public class SalesOrder {
    private String orderId;
    private int memberId;
    private LocalDateTime orderDatetime;
    private int stateCode;

    private String recipient;
    private String phone;
    private String addressA;
    private String addressB;
    private String postcode;
    private String deliveryRequest;

    // 결제
    private Long paymentId;
    private String paymentMethod;
    private int paymentAmount;

    // 주문 상품
    private List<SalesOrderProduct> products;
}