package com.example.backend.config.auth;

import com.example.backend.domain.user.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/script/**", "image/**", "/fonts/**", "lib/**");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/","/css/**", "/images/**", "/js/**").permitAll()     // 이미지 파일 등은 모두 접근할 수 있게 허용
                .antMatchers("/api/boards/**").hasRole(RoleType.NORMAL.name())        // NOTE: 해당 사용자만 글쓰기,수정 허용
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()                                       // 설정된 값들 이외 나머지 URL은 모두 인증된 사용자들만 허용
                .and()
                .logout()
                .logoutSuccessUrl("/") // 로그아웃 성공시 홈으로 이동
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService); // 소셜 로그인 성공 시 후속 조치를 진행할 CustomOAuth2UserService(UserService 인터페이스의 구현체) 등록


    }
}
