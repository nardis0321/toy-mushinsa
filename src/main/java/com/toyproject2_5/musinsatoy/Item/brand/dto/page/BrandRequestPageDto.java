package com.toyproject2_5.musinsatoy.Item.brand.dto.page;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BrandRequestPageDto {

    private int pageNum;

    private int countPerPage;

    private String keyword;

    //byDate, starRate, sales, price
    //되는지 먼저 보고 orderName = p.create_datetime, p.star_rating등으로 바꿔서 넣기.

    /* pagePaging
    * <when test ="orderName == new ">
            create_datetime DESC
    * </when>
    * sortName = new(신상 순), sales(판매량 순), ranking(별점 순)...
    *
    * */
    private String sortCode;



    public BrandRequestPageDto() {
        this.pageNum = 1;
        this.countPerPage = 9;
    }
}
