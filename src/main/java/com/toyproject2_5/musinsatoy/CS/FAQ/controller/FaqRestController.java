package com.toyproject2_5.musinsatoy.CS.FAQ.controller;

import com.toyproject2_5.musinsatoy.CS.FAQ.dto.FaqDto;
import com.toyproject2_5.musinsatoy.CS.FAQ.service.FaqService;
import com.toyproject2_5.musinsatoy.ETC.FaqSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cs/faq")
@RequiredArgsConstructor
public class FaqRestController {
    private final FaqService faqService;
    //1. FAQ 리스트를 보내는 메서드 (챗봇 api)
    @GetMapping("/allApi")
    @CrossOrigin(origins = "http://localhost:5000")
    public List<FaqDto> getAllFaqs(){
        try{
            System.out.print("aaaaa");
            List<FaqDto> faqDtoList = faqService.selectAllAsc();
            faqDtoList.forEach(System.out::println);


            return faqDtoList;
        } catch(Exception e){
            e.printStackTrace();
            //에러 DTO 만들어야 할까
            return null;
        }
    }

    //2. 검색 메서드
    @GetMapping("/searchApi")
    public List<FaqDto> searchFaqs(@RequestParam("q") String keyword){
        try{
            FaqSearchCondition fsc = new FaqSearchCondition(keyword);

            List<FaqDto> faqSearchedList = faqService.searchSelect(fsc);

            return faqSearchedList;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //3. 카테고리 필터링 메서드
    @GetMapping("/listApi/{category_code}")
    public List<FaqDto> categoryCodeList(@PathVariable("category_code") String category_code){
        try{
            List<FaqDto> faqDtoDescCatCodeList = faqService .selectAllCatCodeDesc(category_code);

            return faqDtoDescCatCodeList;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
