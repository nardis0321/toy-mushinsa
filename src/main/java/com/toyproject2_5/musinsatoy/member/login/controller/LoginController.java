package com.toyproject2_5.musinsatoy.member.login.controller;

import com.toyproject2_5.musinsatoy.member.join.dto.AdminBrandDto;
import com.toyproject2_5.musinsatoy.member.login.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/member")
public class LoginController {
    @Autowired
    private LoginService loginService;
//
//    @Autowired
//    JwtEncoder encoder;
//
//    @PostMapping("/tokenLogin")
//    public String tokenLogin(Authentication authentication){
//        Instant now = Instant.now();
//        long expiry = 36000L;
//        String scope = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(" "));
//        JwtClaimsSet claims = JwtClaimsSet.builder()
//                .issuer("self")
//                .issuedAt(now)
//                .expiresAt(now.plusSeconds(expiry))
//                .subject(authentication.getName())
//                .claim("scope", scope)
//                .build();
//        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
//    }


    @GetMapping("/login")
    public String showLogin(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", error);
        }
        return "login/login"; // login.html
    }

    @PostMapping("/login")
    public String login(@RequestParam("id") String id, @RequestParam("password") String password
                        , Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        Object loginResult = loginService.loginCheck(id, password);

        if (loginResult instanceof Map) { // 로그인 성공하면
            Map<String, String> resultMap = (Map<String, String>) loginResult;
            session.setAttribute("id", id);
            session.setAttribute("isAdmin", resultMap.get("isAdmin"));
            session.setAttribute("brand", resultMap.get("brand"));

            model.addAttribute("id", id);
            return "index"; // 메인 페이지로 리다이렉트
        } else {
            // 로그인 실패 시 오류 메시지를 리다이렉트 시 파라미터로 전달
            redirectAttributes.addAttribute("error", loginResult);
            return "redirect:/member/login";
        }

    }
}
