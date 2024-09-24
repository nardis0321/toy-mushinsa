package com.toyproject2_5.musinsatoy.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.toyproject2_5.musinsatoy.member.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.public.key}")
    RSAPublicKey publicKey;

    @Value("${jwt.private.key}")
    RSAPrivateKey privateKey;

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http

                // disable csrf and use token 이하 주석 공식예시
                //                .csrf((csrf) -> csrf.ignoringRequestMatchers("/token")) - 왜냐면 예시에서는 token으로 접근
                .csrf(AbstractHttpConfigurer::disable)

                // 권한 체크 부분 api dos 보고 추가해야 함
                .authorizeHttpRequests((auth)->auth
                        .requestMatchers("/", "/login", "/css/**",
                                "/html/**", "/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/product/**").permitAll()
                    .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/member/login")  // 커스텀 로그인 페이지 경로
                        .permitAll())

                // HTTP Basic 인증 방식 (헤더에 사용자 이름과 비밀번호를 Base64로 인코딩하여 전달)
                .httpBasic(Customizer.withDefaults())

                //  OAuth2 리소스 서버 설정 (JWT를 사용하여 OAuth2 토큰을 처리)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder()) // 위에 선언한 jwt 디코더
                        )
                )

                // disable session and use token
                .sessionManagement((session) -> session.sessionCreationPolicy((SessionCreationPolicy.STATELESS)))

//              예외처리
                .exceptionHandling( (exception) -> exception
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                )
                        ;
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(CustomUserDetailsService customUserDetailsService) {
        return customUserDetailsService;
    }

}
