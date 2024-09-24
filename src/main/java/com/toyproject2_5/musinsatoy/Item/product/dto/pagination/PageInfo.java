package com.toyproject2_5.musinsatoy.Item.product.dto.pagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@Getter
@Setter
@ToString
public class PageInfo {

    //5의 배수 씩 보여주는 페이지.
    //첫번째 페이지와 맨 마지막 페이지로 이동 없음.

    private ProductRequestPageDto paging;
    private int productTotalCount;
    private int endPage;
    private int beginPage;
    private boolean next;
    private boolean prev;
    private int nextPage;
    private int prevPage;

    private final int buttonNum = 5;


    private void calcDataOfPage() {

        //현재 페이지 기준 표시할 마지막 페이지
        // 현재 페이지가 2 이면 5까지 표시.
        //현재 페이지가 6이면 10까지 표시.
        //현재 페이지가 5면 5까지 표시
        //현재페이지가 1이면 5까지 표시
        endPage = (int) (Math.ceil(paging.getPageNum() / (double) buttonNum) * buttonNum);

        //현재 페이지 기준 표시할 처음 페이지
        //현재 페이지가 2 이면 마지막 페이지가 5였으므로 begin page는 1
        // 현재 페이지가 6이면 마지막 페이지가 10이었으므로 begin page는 6
        beginPage = (endPage - buttonNum) + 1 ;



        //앞으로 5쪽 씩 이전 페이지로 이동이 가능한지.
        //현재 페이지가 12 이면 6 ~ 10 까지의 페이지를 보여주고 10 의 페이지로 이동할 수 있도록.
        /*
        * prev 버튼 클릭시 페이지 수식
        * requestPageNumber = Math.ceil((currentPageNumber - 5 / (duoble) buttonNum) * buttonNum)
        * -5 는 currentPageNumber에 하든 requestPageNumber에 하든 상관 없음.
        * */
        prev = (beginPage != 1);
        if(prev){
            prevPage = beginPage-1;
        }else{
            prevPage = beginPage;
        }




        //false 가 되면 다음 쪽으로 가는 '>' 버튼 없애던가 눌러도 안되게.
        //5쪽 씩 다음 페이지로 이동이 가능한지
        //현재 페이지가 12 이면  16~20 까지의 페이지를 보여주고 16의 페이지로 이동할 수 있도록.
        next = productTotalCount > (endPage * paging.getCountPerPage());




        //5쪽씩 다음 페이지로 못넘어가는 경우.
        /*
        * size가 4
        * 현재 페이지가 7
        * totalProduct가 35개 일경우
        * 6,7,8,9 까지만 보여주기. (begin page = 6,  endpage = 9)
        * */
        if (!next) {
            endPage = (int) Math.ceil(productTotalCount / (double) paging.getCountPerPage())==0?beginPage:(int) Math.ceil(productTotalCount / (double) paging.getCountPerPage());
            nextPage = endPage;

        }else{
            nextPage = endPage + 1;

        }

    }

    public void productCountCal(int productTotalCount) {
        this.productTotalCount = productTotalCount;
        calcDataOfPage();
    }

    public static HashMap<String, Object> toHashMap(PageInfo pageInfo) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("offset",(pageInfo.getPaging().getPageNum() -1)*pageInfo.getPaging().getCountPerPage());
        hashMap.put("size",pageInfo.getPaging().getCountPerPage());
        hashMap.put("totalCount",pageInfo.getProductTotalCount());
        hashMap.put("brandId",pageInfo.getPaging().getBrandId());
        hashMap.put("categoryId",pageInfo.getPaging().getCategoryId());
        //DESC, ASC를 그대로 가져오는게 좋은지 서버에서 DESC, ASC로 변환하는게 좋은지.
        hashMap.put("sortCode", pageInfo.getPaging().getSortCode());


        return hashMap;
    }
}