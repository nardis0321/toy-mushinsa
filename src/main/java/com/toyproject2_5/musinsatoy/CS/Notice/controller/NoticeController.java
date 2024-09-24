package com.toyproject2_5.musinsatoy.CS.Notice.controller;


import com.toyproject2_5.musinsatoy.CS.Notice.dto.NoticeDto;
import com.toyproject2_5.musinsatoy.CS.Notice.service.NoticeService;
import com.toyproject2_5.musinsatoy.ETC.CSPageHandler;
import com.toyproject2_5.musinsatoy.Validator.CSDtoCheck;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO - admin 판별 부분이 겹치는 것이 많다. 밖으로 빼보자

@Controller
@RequestMapping("/cs/notice")
@RequiredArgsConstructor //private final인 객체에 대해서 생성자 만들어줌(생성자 주입방식) - autowired를 대체
public class NoticeController {
    private final NoticeService noticeService;

    //1.입력화면(get, insert)
    @GetMapping("/insert")
    public String insert(HttpSession session, Model model, RedirectAttributes rattr){
        try{
            //1) 로그인 유무 판별
            if(session.getAttribute("id") == null) {
                rattr.addFlashAttribute("msg", "Not Signed In");
                return "redirect:/member/login";
            }
            //2) session에서 is_admin "A"인지 판별
            if(!(session.getAttribute("isAdmin").equals("A"))){
                rattr.addFlashAttribute("msg", "NOT_ADMIN");
                return "redirect:/cs/notice/list";
            }

            //3) session에서 id 가져와서 저장
            String member_Id = (String)session.getAttribute("id");

            //4) model에 id 담기
            model.addAttribute("member_Id", member_Id);
            return "Notice/insert";
        }catch(Exception e) {
            //위에서 잡지 못한 에러들 잡기
            e.printStackTrace();
            rattr.addFlashAttribute("msg", "NOTICE_INSERT_MV_UNKNOWN");
            //공지 list로 redirect
            return "redirect:/cs/notice/list";
        }
    }

    //1-2.저장(post, insert)
    @PostMapping("/insert") //예외처리(멤버 정보 못가져왔을때)
    public String insert(HttpSession session, NoticeDto noticeDto, Model m, RedirectAttributes rattr){
        //관리자인지 확인
        //1)로그인 상태인지
        if(session.getAttribute("id") == null) {
            rattr.addFlashAttribute("msg", "Not Signed In");
            return "redirect:/member/login";
        }
        //2) session에서 is_admin "A"인지 판별
        if(!(session.getAttribute("isAdmin").equals("A"))){
            rattr.addFlashAttribute("msg", "NOT_ADMIN");
            return "redirect:/cs/notice/list";
        }
        String writer = (String)session.getAttribute("id");

        //3) 맞으면 noticeDto의 writer에 member_number 저장하고(noticeDto.setWriter(writer)) insert.html
        try {
            //도메인 검증
            CSDtoCheck.validateNoticeDto(noticeDto);
            noticeDto.setWriter(writer);

            //1)noticeDto 삽입, 실패시 예외 던지기
            if (noticeService.insert(noticeDto) != 1) throw new Exception("Service.insert() failed");
            //2)성공시 메시지 addFlashAttribute
            rattr.addFlashAttribute("msg", "INSERT_OK");
            //3)list 로 redirect
            return "redirect:/cs/notice/list";
        } catch (Exception e) {
            e.printStackTrace(); //에러 내용 출력
            m.addAttribute(noticeDto); //입력한 내용 기억
            m.addAttribute("msg", "INSERT_ERR"); //에러 메시지
            return "Notice/insert"; //등록하려던 내용을 보여줘야하므로 insert.html
        }
    }

    //2.리스트조회(list) - 모두 볼 수 있다
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize,Model m){
        try {
            //pagination
            //1)게시글수 파악
            int totalCnt = noticeService.getCount();
            m.addAttribute("totalCnt", totalCnt);

            //2)페이지핸들러 호출
            CSPageHandler ph = new CSPageHandler(totalCnt, page, pageSize);

            if(page < 0 || page > ph.getTotalPage())
                page = 1;
            if(pageSize < 0 || pageSize > 50)
                pageSize = 10;

            //3)map에 offset, pageSize 저장(일회용인거 기억하기)
            Map map = new HashMap();
            map.put("offset", (page - 1) * pageSize);
            map.put("pageSize", pageSize);

            //4)공지 상단 게시 리스트
            List<NoticeDto> noticeDtoTopPostList = noticeService.selectAllTopPost();
            m.addAttribute("noticeDtoTopPostList", noticeDtoTopPostList);

            //5) 공지 하단 게시 리스트
            List<NoticeDto> noticeDtoDescPageList = noticeService.selectAllDescPage(map);
            if(noticeDtoDescPageList.isEmpty()) throw new Exception("Service.pagelist() empty");
            m.addAttribute("noticeDtoDescPageList", noticeDtoDescPageList);
            m.addAttribute("pageHandler", ph);
        } catch (Exception e) {
            e.printStackTrace();
            //6)리스트가 비어있거나 가져오는데 실패하면 에러 메시지
            m.addAttribute("msg", "LIST_ERR");
            m.addAttribute("totalCnt", 0);
        }
        //7)성공이든 실패든 공지사항 리스트를 보여준다.
        return "Notice/list"; //6) list.html
    }

    //3.단일 조회 - 모두 볼 수 있다
    @GetMapping("/{id}")
    public String selectById(@PathVariable("id") Long id, Model m, RedirectAttributes rattr){
        try{
            NoticeDto noticeDto = noticeService.selectById(id); //1) id로 해당 noticeDTO찾기
            if(noticeDto == null) {
                throw new Exception("service.selectbyid not found"); //1-1) 못찾으면 에러 표시하고 나간다.
            }
            m.addAttribute("notice", noticeDto);    // 2) noticeDTO 모델에 저장
        } catch (Exception e) {
            e.printStackTrace(); //1)에러 확인
            rattr.addFlashAttribute("msg", "READ_ERR"); //2)에러 메시지
            return "redirect:/cs/notice/list"; //3) 리스트로 리다이렉트
        }
        return "Notice/detail"; //3)detail.html 이동
    }

    //4.update 화면 이동 - 관리자만 가능, 예외처리
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long notice_id, HttpSession session, Model m, RedirectAttributes rattr){
        //1) 로그인 유무 판별
        if(session.getAttribute("id") == null) {
            rattr.addFlashAttribute("msg", "NOT_SIGNED_IN");
            return "redirect:/member/login";
        }
        //2) session에서 is_admin "A"인지 판별
        if(!(session.getAttribute("isAdmin").equals("A"))){
            rattr.addFlashAttribute("msg", "NOT_ADMIN");
            return "redirect:/cs/notice/list/{id}";
        }
        //3) session에서 id 가져와서 저장
        String member_Id = (String)session.getAttribute("id");

        try{
            NoticeDto noticeDto = noticeService.selectById(notice_id); //1) id로 update할 noticeDTO찾기
            if(noticeDto == null) {
                throw new Exception("update select not found"); //1-1) 못찾으면 에러 표시하고 나간다.
            }
            noticeDto.setModifier(member_Id);//modifier 설정
            m.addAttribute("notice", noticeDto); //2)모델에 추가
            return "Notice/update";
        } catch (Exception e) {
            e.printStackTrace(); //에러 확인
            rattr.addFlashAttribute("msg", "UPDATE_MV_ERR"); //에러 표시
            return "redirect:/cs/notice/list"; //못찾거나 update작성 화면으로 못넘어가므로 list로 redirect
        }
    }

    //4-2.update 요청(POST) - 관리자만 가능, 예외처리
    @PostMapping("/update/{id}")
    public String update(HttpSession session, NoticeDto noticeDto, Model m, RedirectAttributes rattr) {
        //1) 로그인 유무 판별
        if(session.getAttribute("id") == null) {
            rattr.addFlashAttribute("msg", "NOT_SIGNED_IN");
            return "redirect:/member/login";
        }
        //2) session에서 is_admin "A"인지 판별
        if(!(session.getAttribute("isAdmin").equals("A"))){
            rattr.addFlashAttribute("msg", "NOT_ADMIN");
            return "redirect:/cs/notice/list/{id}";
        }
        try{
            //도메인 검증
            CSDtoCheck.validateNoticeDto(noticeDto);

            if(noticeService.update(noticeDto) != 1) throw new Exception("service.update() failed");    //1. noticeDTO update실행
            rattr.addFlashAttribute("msg", "UPDATE_OK");                       //2. 성공시 메시지 보내기
            return "redirect:/cs/notice/{id}";                                                              //3. 상세화면으로 돌아간다.
        } catch (Exception e) {
            e.printStackTrace();//1. 실패시 에러 출력
            m.addAttribute(noticeDto); //입력한 내용 기억
            m.addAttribute("msg", "UPDATE_ERR");                               //2. 에러 메시지 보내기
            return "Notice/update";                                                                            //3. 수정화면으로 돌아간다.
        }
    }

    //5.삭제 요청(GET) - 관리자만 가능, 예외처리
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, HttpSession session, Model m, RedirectAttributes rattr){
        //1) 로그인 유무 판별
        if(session.getAttribute("id") == null) {
            rattr.addFlashAttribute("msg", "NOT_SIGNED_IN");
            return "redirect:/member/login";
        }
        //2) session에서 is_admin "A"인지 판별
        if(!(session.getAttribute("isAdmin").equals("A"))){
            rattr.addFlashAttribute("msg", "NOT_ADMIN");
            return "redirect:/cs/notice/list/{id}";
        }

        //관리자의 id 저장, dto.setModifier(id)
        try{
            if(noticeService.delete(id, (String)session.getAttribute("id")) != 1) throw new Exception("service.delete() failed"); //1) 해당 id delete() 호출
            rattr.addFlashAttribute("msg", "DELETE_OK");             //2) 성공 메시지 보내기
            return "redirect:/cs/notice/list";                                                //3) 리스트로 돌아간다.
        } catch (Exception e) {
            e.printStackTrace();                                                              //1) 실패시 에러 출력
            rattr.addFlashAttribute("msg", "DELETE_ERR");            //2) 에러 메시지 보내기
            return "Notice/detail";                                                           //3) 상세 화면으로 돌아가기
        }
    }
}