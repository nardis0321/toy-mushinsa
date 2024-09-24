package com.toyproject2_5.musinsatoy.Item.product.controller;

import com.toyproject2_5.musinsatoy.Item.category.dto.Category;
import com.toyproject2_5.musinsatoy.Item.category.dto.SubCategoryDto;
import com.toyproject2_5.musinsatoy.Item.category.service.CategoryService;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.hasNextOffset.SearchProductDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.Page;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.adminEdit.EditPageInfo;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.adminEdit.ProductEditPageDto;
import com.toyproject2_5.musinsatoy.Item.product.service.ProductService;
import com.toyproject2_5.musinsatoy.Item.product.dto.ProductAdminList;
import com.toyproject2_5.musinsatoy.Item.product.dto.ProductDetailDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;


    //페이지마다 컨트롤러가 하나씩 있는게 아님. (랭킹, 세일, 이벤트 등등 숫자로 구분) - 무신사

    /*
    * 일단 모름.
    * page 번호 넘겨줘서 페이지 번호에 따라 sortCode Service에서 변경하기.
    * */
    @GetMapping("/{pageNumber}")
    public String mainpage(@PathVariable int pageNumber, Model model){

        //state패턴 적용 가능한지 확인.
        if(pageNumber== Page.RANKINGPAGE){
            /*
            * 어느정도 정보 주기.
            * */
            return "main/ranking";
        }else if(pageNumber == Page.NEWPAGE){
            return "main/new";
        }else if(pageNumber == Page.SALESPAGE){
            return "main/saleQuantity";
        }else if(pageNumber == Page.LOWPRICE){
            return "main/lowPrice";
        }else if(pageNumber == Page.MAINPAGE){
            return "main/main";
        }

        return"redirect:/product/100";
    }


    /*
    *   상품 상세 페이지 연결
    *
    *   상품 코드를 숫자로 할지 생각.
    *   디테일 페이지에 들어가면 자동으로 조회수 증가 하도록
    *
    *   detail page 옵션 어떻게 보여줄건기 - option 이랑  재고랑 조인하긴 하는데 option 마지막 계층이랑
    *   stock의 product_option_id 랑 어떻게 짝지을 건지. - sql 에서 하나씩 where 절로 재고랑 상품 상태 찾기?
    *
    * */
    @GetMapping("/detail/{productId}")
    public String productDetailPage(@PathVariable String productId, Model model) {
            try {

                model.addAttribute("productDetail", productService.findProductDetailById(productId));

                //일단 조회수 중복 증가 방지를 위해 따로 빼둠. -> 나중에 cookie 등으로 중복 증가 하지 못하게등 생각해보기.
                productService.viewCount(productId);

            }catch (Exception e){
                e.printStackTrace();
                //해당 상품을 못찾으므로 error페이지를 띄워주거나 error 메세지를 클라이언트에게 띄우기.
                return "error";
            }
        return "product/productDetail";
    }

    /*
    *
    * 상품 등록 페이지
    *
    * (홈페이지 관리자, 각 브랜드 담당자, 일반 회원)
    * 해당 브랜드의 사람이 등록을 할건데. 브랜드 Id를 path로 주는게 맞는지.
    * */
    @GetMapping("/admin/register")
    public String adminRegisterPage(Model model, HttpSession session) throws Exception {

        //front에서 maneger name에는 id 입력, brandID에는 session brand 가져오기.
        // 로그인을 하지 않았거나, 그냥 유저인사람은 불가.
        if(session.getAttribute("isAdmin") =="U" || session.getAttribute("id")== null){
            return "redirect:/";
        }

        //상세 설명 가져오기. - 해당 브랜드의 상세설명을 고를 수 있게 해도 ㄱㅊ을 듯.

        // 초반 카테고리 항목 설정. - 해당 카테고리의 하위 카테고리는 API로 계속 가져오도록 하기.
        List<SubCategoryDto> categoryList = categoryService.findSubCategory(Category.FIRSTCATEGORY);

        model.addAttribute("categoryList", categoryList);

        return "product/register";
    }



    /*
    * 등록한 상품 목록 페이지 - 등록 상품 목록 페이지에 수정, 제거 버튼 넣기.
     *
     *
    * */

    @GetMapping("/admin/list")
    public String adminProductList(Model model, ProductEditPageDto productEditPageDto, HttpSession session) throws Exception {

        //session이 없거나 로그인 한 유저가 관리자가 아닐 시.
        if(session== null || session.getAttribute("isAdmin") =="U"){
            return "redirect:/";
        }
        //브랜드 관리자의 Session에서 brandId 로 DB상품 조회 가능하게 저장.
        productEditPageDto.setBrandId((String) session.getAttribute("brand"));

        //관리자 상품 수정 페이지 정보 생성(마지막 페이지, 다음 페이지 여부등)
        EditPageInfo editPageInfo = new EditPageInfo();
        editPageInfo.setProductEditPageDto(productEditPageDto);


        List<ProductAdminList>  productAdminList = productService.findProductAdminList(editPageInfo);

        model.addAttribute("products", productAdminList);
        model.addAttribute("pageInfo",editPageInfo);
        return "product/adminProductList";
    }

    @GetMapping("test")
    public String S3ImageTest(){
        return "test";
    }





    /*
    *  상품 수정 페이지
    *  미완 - 상세 설명 수정, -이미지 순서 정렬 수정(카카오톡 이모티콘 처럼 수정과 삭제 분리하기.)
    * */

    @GetMapping("/admin/{productId}")
    public String adminEditProduct(@PathVariable("productId") String productId, Model model) throws Exception {

        //해당 productId의 상세 정보 불러오기.
        ProductDetailDto productData =productService.findProductDetailById(productId);
        model.addAttribute("productData", productData);

        //C001 의류 의 한단계 아래 카테고리 목록 불러오기.
        List<SubCategoryDto> categoryList = categoryService.findSubCategory(Category.FIRSTCATEGORY);
        model.addAttribute("categoryList", categoryList);

        return "product/adminProductEdit";
    }

    /*
    * 키워드로 검색시 나타낼 화면
    * 미완
    * */
    @GetMapping("/search")
    public String searchPage(Model model, @ModelAttribute  SearchProductDto searchProductDto) throws Exception {
        model.addAttribute("category",categoryService.findSubCategory("C01"));
        return "product/searchPage";
    }









}
