package com.toyproject2_5.musinsatoy;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 1. 원격 프로그램으로 등록
@Controller
public class HomeController { // 원격 프로그램
    // 2. URL과 메서드를 연결
    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        String loginId = (String) session.getAttribute("id");
        model.addAttribute("id", loginId);
        return "index";
    }

    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/"; // 로그아웃 후 홈으로 리다이렉트
    }

}
