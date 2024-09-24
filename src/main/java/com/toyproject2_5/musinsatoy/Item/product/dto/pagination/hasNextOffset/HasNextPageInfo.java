package com.toyproject2_5.musinsatoy.Item.product.dto.pagination.hasNextOffset;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HasNextPageInfo {


  /*
    api로 커서기반 처럼.
    총 상품의 개수를 이용해 총 페이지를 구하고.
    hasNextPage가 false가 되면 중단.
    무신사에서는 아래만 넘겨줌.
*/

    private boolean hasNextPage;
    private int totalCounts;

    //SearchKeywordDto로부터 받기
    private int page;
    private int size;
    private int totalPages;

    public void calNext(int totalCounts){
        this.totalCounts = totalCounts;
        this.totalPages = (int) Math.ceil((double) totalCounts /size);
        hasNextPage = (page!=totalPages);
    }


}
