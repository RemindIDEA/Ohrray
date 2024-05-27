package com.ohrray.config;


import com.ohrray.security.handler.CustomAccessDeniedHandler;
import com.ohrray.security.handler.LoginSuccessHandler;
import com.ohrray.security.handler.CustomAuthenticationFailureHandler;
import com.ohrray.security.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true ,prePostEnabled = true)
public class SecurityConfig {
    //비밀번호 암호화


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 현재 depricated됨. 람다식사용으로 바뀜. controller에서 annotation사용해서 처리 추천.
//        http.authorizeHttpRequests()
//                .requestMatchers("/sample/all").permitAll()
//                .requestMatchers("/sample/member").hasRole("USER")
//                .requestMatchers("/sample/admin").hasRole("ADMIN");

        http
                .formLogin((formLogin) -> formLogin
                        .loginPage("/member/login")//로그인 페이지로 이동
                        .defaultSuccessUrl("/shopping/main", false)
                        .successHandler(loginSuccessHandler())
                        .failureHandler(customAuthenticationFailureHandler()))
                .oauth2Login(oauth2->oauth2.successHandler(loginSuccessHandler()));
        ;

        http.rememberMe((rememberMe) -> rememberMe
                .tokenValiditySeconds(60*60*24*7).userDetailsService(userDetailsService));

        http.logout((logout)->logout
                .logoutUrl("/logout").logoutSuccessUrl("/member/login"));
        http.exceptionHandling((exception)
                ->exception.accessDeniedHandler(customAccessDeniedHandler())
        );
        return http.build();
    }
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

}
