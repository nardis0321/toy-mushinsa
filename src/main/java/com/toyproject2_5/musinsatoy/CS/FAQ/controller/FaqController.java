package com.toyproject2_5.musinsatoy.CS.FAQ.controller;

import com.toyproject2_5.musinsatoy.CS.FAQ.dto.FaqDto;
import com.toyproject2_5.musinsatoy.CS.FAQ.service.FaqService;
import com.toyproject2_5.musinsatoy.ETC.FaqSearchCondition;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO
//2. 공통 예외처리 Exception Handler
//3. 전역 예외 처리 GlobalCatcher(@ControllerAdvice)

@Controller
@RequestMapping("/cs/faq")
@RequiredArgsConstructor
public class FaqController {
    private final FaqService faqService;

    //1.입력화면
    //1-1.입력화면 이동 - GET
    @GetMapping("/insert")
    public String insert(HttpSession session, Model model, RedirectAttributes rattr){
        try{
            //1) 로그인 유무 판별
            if(session.getAttribute("id") == null) {
                rattr.addFlashAttribute("msg", "NOT_SIGNED_IN");
                return "redirect:/member/login";
            }
            //2) session에서 is_admin "A"인지 판별
            if(!(session.getAttribute("isAdmin").equals("A"))){
                rattr.addFlashAttribute("msg", "NOT_ADMIN");
                return "redirect:/cs/faq/list";
            }
            //3) session에서 id가져와서 저장
            String admin_id = (String)session.getAttribute("id");
            //4) Model 에 id 저장
            model.addAttribute("admin_id", admin_id);

            return "FAQ/insert";
        } catch( Exception e ){
            e.printStackTrace(); //에러 내용 출력
            rattr.addFlashAttribute("msg", "FAQ_INSERT_MV_ERROR"); //에러 메시지
            return "redirect:/cs/faq/list";
        }
    }

    //1-2.입력수행 - POST
    @PostMapping("/insert")
    public String insert(HttpSession session, FaqDto faqDto, Model m, RedirectAttributes rattr){
        try{
            //1) 로그인 유무 판별
            if(session.getAttribute("id") == null) {
                rattr.addFlashAttribute("msg", "NOT_SIGNED_IN");
                return "redirect:/member/login";
            }
            //2) session에서 is_admin "A"인지 판별
            if(!(session.getAttribute("isAdmin").equals("A"))){
                rattr.addFlashAttribute("msg", "NOT_ADMIN");
                return "redirect:/cs/faq/list";
            }

            if(faqService.insert(faqDto) != 1) throw new Exception("Service.insert() failed");
            rattr.addFlashAttribute("msg", "INSERT_OK");

            return "redirect:/cs/faq/list";
        } catch(Exception e){
            e.printStackTrace(); //에러 내용 출력
            m.addAttribute(faqDto); //입력한 내용 기억
            m.addAttribute("msg", "INSERT_ERR"); //에러 메시지
            return "FAQ/insert"; //등록하려던 내용을 보여줘야하므로 insert.html
        }
    }

    //2.faq상세 /id
    @GetMapping("/{id}")
    public String selectById(@PathVariable("id") Integer id, Model model, RedirectAttributes rattr){
        try{
            FaqDto faqDto = faqService.selectById(id);
            if(faqDto == null){
                throw new Exception("Service.selectById not found");
            }
            model.addAttribute("faqDto", faqDto);
        } catch(Exception e){
            e.printStackTrace();
            rattr.addFlashAttribute("msg", "READ_ERR");
            return "redirect:/cs/faq/list";
        }
        return "FAQ/detail";
    }

    //3.faq리스트 전체 내림차순 (/faq)
    @GetMapping("/list")
    public String list(Model m){
        try {
            List<FaqDto> faqDtoDescList = faqService.selectAllCatDesc();
            faqDtoDescList.forEach(System.out::println);
//            if(faqDtoDescList.isEmpty()) throw new Exception("Service.DescList() empty");
            m.addAttribute("faqDtoDescList", faqDtoDescList);
        } catch(Exception e ) {
            e.printStackTrace();
            m.addAttribute("msg", "LIST_ERR");
        }

        return "FAQ/list";
    }

    //3-1.faq리스트 카테고리 코드 필터링 (/faq/004) requestparam
    @GetMapping("/list/{category_code}")
    public String categoryCodeList(@PathVariable("category_code") String category_code, Model m){
        try {
            List<FaqDto> faqDtoDescCatCodeList = faqService.selectAllCatCodeDesc(category_code);
//            if(faqDtoDescCatCodeList.isEmpty()) throw new Exception("Service.CatCodeDescList() empty");
            m.addAttribute("faqDtoDescCatCodeList", faqDtoDescCatCodeList);
        } catch(Exception e ) {
            e.printStackTrace();
            m.addAttribute("msg", "LIST_ERR");
        }
        return "FAQ/category_filter_list";
    }

    //3-2.faq리스트 카테고리 코드, 소분류id 필터링 (/faq/004/004001)
    @GetMapping("/list/{category_code}/{subcategory_id}")
    public String subCategoryIdList(@PathVariable("category_code") String category_code, @PathVariable("subcategory_id") Integer subcategory_id, Model m){
        try {
            Map categoryMap = new HashMap();
            categoryMap.put("faq_category_code", category_code);
            categoryMap.put("faq_subcategory_id", subcategory_id);

            List<FaqDto> faqDtoDescCatCodeSubIdList = faqService.selectAllCatCodeSubIdDesc(categoryMap);
//            if(faqDtoDescCatCodeSubIdList.isEmpty()) throw new Exception("Service.CatCodeSubIdDescList() empty");
            m.addAttribute("faqDtoDescCatCodeSubIdList", faqDtoDescCatCodeSubIdList);
        } catch(Exception e ) {
            e.printStackTrace();
            m.addAttribute("msg", "LIST_ERR");
        }
        return "FAQ/subcategory_filter_list";
    }

    //3-3.faq리스트 검색 필터링 (/searchList?q="")
    @GetMapping("/searchList")
    public String searchList(@RequestParam("q") String keyword, Model m){
        try {
            FaqSearchCondition fsc = new FaqSearchCondition(keyword);

            List<FaqDto> faqSearchedList = faqService.searchSelect(fsc);

            int searchCount = faqService.searchResultCnt(fsc);

            m.addAttribute("faqSearchedList", faqSearchedList);
            m.addAttribute("searchCount", searchCount);
        } catch(Exception e ) {
            e.printStackTrace();
            m.addAttribute("msg", "LIST_ERR");
        }
        return "FAQ/search_filter_list";
    }
    //4. 수정
    //4-1. 수정화면 이동 - GET
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Integer faq_id, HttpSession session, Model m, RedirectAttributes rattr){
        try{
            //1) 로그인 유무 판별
            if(session.getAttribute("id") == null) {
                rattr.addFlashAttribute("msg", "NOT_SIGNED_IN");
                return "redirect:/member/login";
            }
            //2) session에서 is_admin "A"인지 판별
            if(!(session.getAttribute("isAdmin").equals("A"))){
                rattr.addFlashAttribute("msg", "NOT_ADMIN");
                return "redirect:/cs/faq/list";
            }
            //3) session에서 id가져와서 저장
            String admin_id = (String)session.getAttribute("id");

            FaqDto faqDto = faqService.selectById(faq_id);
            if(faqDto == null) throw new Exception("Service.update() selectById failed");

            //4) admin_id, faqDto model 추가
            m.addAttribute("admin_id", admin_id);
            m.addAttribute("faqDto", faqDto);

            return "FAQ/update";
        } catch (Exception e){
            e.printStackTrace();
            rattr.addFlashAttribute("msg", "UPDATE_MV_ERR");
            return "redirect:/cs/faq/list";
        }
    }

    //4-2. 수정수행 - POST
    @PostMapping("/update/{id}")
    public String update(FaqDto faqDto, HttpSession session, Model m, RedirectAttributes rattr){
        try{
            //1) 로그인 유무 판별
            if(session.getAttribute("id") == null) {
                rattr.addFlashAttribute("msg", "NOT_SIGNED_IN");
                return "redirect:/member/login";
            }
            //2) session에서 is_admin "A"인지 판별
            if(!(session.getAttribute("isAdmin").equals("A"))){
                rattr.addFlashAttribute("msg", "NOT_ADMIN");
                return "redirect:/cs/faq/list";
            }

            if(faqService.update(faqDto) != 1) throw new Exception("Service.update() failed");
            rattr.addFlashAttribute("msg", "UPDATE_OK");
            return "redirect:/cs/faq/{id}";
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("faqDto", faqDto);
            rattr.addFlashAttribute("msg", "UPDATE_ERR");
            return "redirect:/cs/faq/{id}";
        }
    }

    //5. 삭제 - GET
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes rattr){
        try{
            //1) 로그인 유무 판별
            if(session.getAttribute("id") == null) {
                rattr.addFlashAttribute("msg", "NOT_SIGNED_IN");
                return "redirect:/member/login";
            }
            //2) session에서 is_admin "A"인지 판별
            if(!(session.getAttribute("isAdmin").equals("A"))){
                rattr.addFlashAttribute("msg", "NOT_ADMIN");
                return "redirect:/cs/faq/list";
            }

            if(faqService.delete(id, (String)session.getAttribute("id")) != 1) throw new Exception("Service.delete() failed");
            rattr.addFlashAttribute("msg", "DELETE_OK");
            return "redirect:/cs/faq/list";
        } catch (Exception e) {
            e.printStackTrace();
            rattr.addFlashAttribute("msg", "DELETE_ERR");
            return "redirect:/cs/faq/{id}";
        }
    }
}
