package com.toyproject2_5.musinsatoy.salesorder.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public record SalesOrderDto(
 // 주문
        @Size(max = 30, message = "ORDER_ID는 30자를 초과할 수 없습니다.")
        String orderId,
        @Positive(message = "memberId는 양수여야 합니다.")
        int memberId,

        LocalDateTime orderDatetime,

        @Min(value = 0, message = "stateCode는 0 이상의 값이어야 합니다.")
        @Max(value = 255, message = "stateCode는 255 이하의 값이어야 합니다.")
        int stateCode,

// 배송
        @Size(max = 30, message = "RECIPIENT는 30자를 초과할 수 없습니다.")
        String recipient,

        @Pattern(regexp = "^\\d{10,15}$", message = "PHONE은 10~15자리 숫자여야 합니다.")
        String phone,

        @Size(max = 80, message = "ADDRESS_A는 80자를 초과할 수 없습니다.")
        String addressA,

        @Size(max = 80, message = "ADDRESS_B는 80자를 초과할 수 없습니다.")
        String addressB,

        @Pattern(regexp = "^\\d{5}$", message = "POSTCODE는 반드시 5자리 숫자여야 합니다.")
        String postcode,

        @Size(max = 80, message = "DELIVERY_REQUEST는 80자를 초과할 수 없습니다.")
        String deliveryRequest,


// 결제
        Long paymentId,

        @Size(max = 15, message = "PAYMENT_METHOD는 15자를 초과할 수 없습니다.")
        String paymentMethod,

        @PositiveOrZero(message = "PAYMENT_AMOUNT는 0 이상의 값이어야 합니다.")
        int paymentAmount,

// 주문 상품
        List<SalesOrderProductDto> products

) {
}
