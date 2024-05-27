package com.ohrray.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Log4j2
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response, AccessDeniedException accessException)
            throws IOException, ServletException {

        //내가 접속했는데 권한이 없을때 어떻게처리? 메인으로 바로?
        //아니면 에러페이지?

        System.out.println("Access Denied Handler");
        response.sendRedirect("/shopping/main");
        //response.sendRedirect("/error"); // error페이지로 이동

    }

}
