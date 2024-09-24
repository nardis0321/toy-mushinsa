package com.toyproject2_5.musinsatoy.Item.brand.controller;


import com.toyproject2_5.musinsatoy.Item.brand.dto.BrandListDto;
import com.toyproject2_5.musinsatoy.Item.brand.dto.page.BrandRequestPageDto;
import com.toyproject2_5.musinsatoy.Item.brand.dto.page.BrandPageInfo;
import com.toyproject2_5.musinsatoy.Item.brand.service.BrandService;
import com.toyproject2_5.musinsatoy.Item.product.dto.ProductPageDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.PageInfo;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.ProductRequestPageDto;
import com.toyproject2_5.musinsatoy.Item.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/brand")
@RequiredArgsConstructor
public class BrandController {

    private final ProductService productService;

    private final BrandService brandService;

    //브랜드 목록화면 - 현재 홈페이지에 등록되어있는 브랜드 들의 목록을 보여줌.
    @GetMapping(value ={ "/main"})
    public String brand(BrandRequestPageDto brandRequestPageDto, Model model) throws Exception {
        //브랜드 목록의 페이지 정보(마지막 페이지, 다음페이지 이동가능 등등)
        BrandPageInfo brandPageInfo = new BrandPageInfo();
        brandPageInfo.setPaging(brandRequestPageDto);

        //모든 브랜드의 정보를 리스트로 저장 - 브랜드별 판매 개수로 정렬
        List<BrandListDto> brandList = brandService.findBrandMainPage(brandPageInfo);
        model.addAttribute("brands", brandList);
        model.addAttribute("pageInfo", brandPageInfo);

        return "brand/brandList";
    }


    /*
    *   브랜드 상세 페이지
    *   선택 한 브랜드의 상품 목록을 보여줌.
    * */
    @GetMapping(value ={ "/{brandCode}"})
    public String brandpage(
            @ModelAttribute @PathVariable(value = "brandCode") String brandId
            , Model model
            , ProductRequestPageDto productRequestPageDto) throws Exception {

        //해당 브랜드의 Id 등록
        productRequestPageDto.setBrandId(brandId);
        //브랜드 상세 페이지의 페이지 정보 설정.
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPaging(productRequestPageDto);
        //해당 브랜드의 상품 검색후 리스트로 저장
        List<ProductPageDto> productPageDtos = productService.findPageList(pageInfo);

        model.addAttribute("brandId",brandId);
        model.addAttribute("productsList", productPageDtos);
        model.addAttribute("pageInfo", pageInfo);

        return "brand/brandPage";
    }

    /*
    * 브랜드 등록 페이지
    * 새로운 브랜드 등록하는 페이지.
    *
    * */
    @GetMapping("/admin/register")
    public String brandRegisterpage() {
        //브랜드 등록 창은 정말 아무런 정보가 안담겨도 괜찮을까


        return "brand/register";
    }


}
