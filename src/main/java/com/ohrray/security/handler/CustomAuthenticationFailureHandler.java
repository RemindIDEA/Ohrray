package com.ohrray.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;

@Log4j2
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception ) throws IOException, ServletException {
        // 로그인 실패 시 로직을 추가
        // 예를 들어 로그를 기록하거나 특정 페이지로 리다이렉트할 수 있음
        // 여기서는 에러 메시지를 사용하여 다시 로그인 페이지로 리다이렉트하는 예시를 보여줌
        System.out.println("로그인이 틀릴경우 만들어야함");

        String errorMessage = "일치하는 회원정보가 없습니다."; // 이대로 날리면 인코딩에러
        String encodedErrorMessage = URLEncoder.encode(errorMessage, "UTF-8");// 이렇게 변환
        response.sendRedirect("/member/login?msg=" + encodedErrorMessage); // 후 날리기 faillogin라는 컨트롤러 통해서 error들고 가서 loginmovie로 보내줌
    }
}