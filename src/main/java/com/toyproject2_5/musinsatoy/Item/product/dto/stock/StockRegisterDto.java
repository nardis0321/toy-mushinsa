package com.toyproject2_5.musinsatoy.Item.product.dto.stock;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class StockRegisterDto {

    private String productId;

    //옵션 이름은 저장 안함. -> 수정시에 계속 업데이트 해줘야함.
    //주문 할때 계속 덧붙여서 주문에서 모든 옵션 계층명을 저장하도록.
    private String optionName;
    private String optionId;
    private int quantity;

}
