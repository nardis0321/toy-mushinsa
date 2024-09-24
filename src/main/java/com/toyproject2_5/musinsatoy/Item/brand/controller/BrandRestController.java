package com.toyproject2_5.musinsatoy.Item.brand.controller;


import com.toyproject2_5.musinsatoy.Item.brand.dto.BrandCreateDto;
import com.toyproject2_5.musinsatoy.Item.brand.dto.BrandUpdateDto;
import com.toyproject2_5.musinsatoy.Item.brand.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
@Tag(name ="브랜드 RestController", description = "브랜드 페이지에서 일어나는 것들.")
public class BrandRestController {

    private final BrandService brandService;



    /*
    * 브랜드 등록 처리
    * */
    @Operation(summary = "브랜드 등록", description = "브랜드 등록하는 api")
    @PostMapping("/admin/{brandId}")
    public ResponseEntity<?> brandRegister(@Validated @RequestBody BrandCreateDto brand) throws Exception {


        String brandImgPath = brandService.createBrand(brand);


        return ResponseEntity.status(HttpStatus.OK).body(brandImgPath);
    }


    @Operation(summary = "브랜드 내용 수정", description = "브랜드 등록과 브랜드 수정에서 받는 파라미터의 차이가 거의 없음.\n\n" +
            "합칠지 따로 둘지 고민.")
    @PatchMapping("/admin")
    public ResponseEntity<?> brandUpdate(@Validated @RequestBody BrandUpdateDto brandUpdateDto) throws Exception {

        brandService.updateBrand(brandUpdateDto);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message",brandUpdateDto.getName() + "브랜드 수정 완료"));
    }




}
