package com.toyproject2_5.musinsatoy.Item.product.dto.pagination.adminEdit;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class ProductEditPageDto {

    private int pageNum;
    private int pageSize;
    private int offset;

    private String brandId;
    private String keyword;
    private String categoryId;

    private String sortCode;

    public ProductEditPageDto(){
        this.pageNum = 1;
        this.pageSize = 10;
    }
    public void setOffset(){
        offset = (pageNum-1)*pageSize;
    }
}
