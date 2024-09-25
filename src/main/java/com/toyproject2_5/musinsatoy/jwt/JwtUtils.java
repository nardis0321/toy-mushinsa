package com.toyproject2_5.musinsatoy.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    public String generateJwtToken(Authentication authentication){
//        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Instant now = Instant.now();
//        long expiry = 3600L; // 1시간
        long expiry = 5L; // 테스트용!!

        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));  // 복수의 role

        JwtClaimsSet claims = JwtClaimsSet.builder()
//                .issuer("self")
//                .subject(userPrincipal.getUsername())
                .subject(authentication.getName())  //username
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .claim("scope", scope)
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public boolean validateJwtToken(String token){
        try {
            // 서명 확인, 만료 확인
            Jwt jwt = this.jwtDecoder.decode(token);

            // 만료 확인
//            Instant now = Instant.now();
//            if (jwt.getExpiresAt() != null && jwt.getExpiresAt().isBefore(now))
//                return false;

            return true;
        } catch(JwtValidationException e){  // 서명 또는 클레임 유효성 문제로 검증 실패
            return false;
        } catch (JwtException e){ // 토큰 파싱 오류 ...
            return false;
        }
    }

    public String getUsernameFromJwt(String token){
        return this.jwtDecoder.decode(token)
                .getSubject();
    }
}
