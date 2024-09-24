package com.toyproject2_5.musinsatoy.Item.product.dto.pagination.adminEdit;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EditPageInfo {

    private final int buttonNum = 5;
    private ProductEditPageDto productEditPageDto;

    //검색한 모든 상품 개수.
    private int countProductNumber;


    //맨 마지막 페이지.
    private int lastPage;

    //화면에 표시될 페이지 숫자
    private int beginPage;
    private int endPage;


    //다음으로 3페이지 이동이 가능한지.
    private boolean isNextPage;

    //이전으로 3페이지 이동이 가능한지.
    private boolean isPreviousPage;


    //현재 페이지 기준으로 앞 뒤 2페이지 씩. - 현재 페이지 ProductEditPageDto의 pagaNum

    public void calEditPageInfo(int countProductNumber){
        this.countProductNumber = countProductNumber;
        setEditPageInfo();
    }

    private void setEditPageInfo(){
        lastPage = (int) Math.ceil(countProductNumber/(double)productEditPageDto.getPageSize());


        beginPage = Math.max(productEditPageDto.getPageNum() - 2, 1);
        endPage = Math.min(beginPage+4, lastPage);

        isNextPage = endPage+1<=lastPage;
        isPreviousPage = beginPage-1>=1;
    }


}
