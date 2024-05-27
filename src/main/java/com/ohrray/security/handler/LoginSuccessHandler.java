package com.ohrray.security.handler;

import com.ohrray.domain.SecurityMemberDTO;
import com.ohrray.entity.Member;
import com.ohrray.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Optional;

@Log4j2
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    //이부분이 소셜로그인 성공했을경우 핸들러 들어가서 여기서 처음이면 정보를 받아주고\
    //두번째부터는 그냥 메인페이지로 가거나 원래 있던 페이지로 갈수있게
    //소셜은 왠만하면 메인페이지부터 들어갈수 있게.

    private RedirectStrategy redirectStratgy = new DefaultRedirectStrategy();
    @Autowired
    private MemberRepository repository;

    private PasswordEncoder passwordEncoder;

    public LoginSuccessHandler(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }
    //로그인이 성공했을때 여기를 들려야 하는데
    //일반로그인일떄는 크게 중요하지 않다.
    //따로 불르고 있는 곳은 없고 Authentication이 성공했을 때 자동으로 부르게 되는 매서드!
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        System.out.println("\"onAuthenticationSuccess\" = " + "onAuthenticationSuccess");

        SecurityMemberDTO authMember = (SecurityMemberDTO)authentication.getPrincipal();
        boolean fromSocial = authMember.isSocial(); // 소셜로그인 여부.

        //정보가져오기 -> principal로 이메일 받아와서
        Optional<Member> member = repository.findByEmailandSocial(authMember.getEmail(),authMember.isSocial());
        System.out.println("member.isPresent() = " + member.isPresent());
        if(member.isPresent()){
            //아이디가 있는경우
            HttpSession session = request.getSession();
            session.setAttribute("email", member.get().getEmail());
            session.setAttribute("password", member.get().getPassword());
            if(fromSocial){
                //소셜로그인인 경우
                boolean passwordResult = passwordEncoder.matches("1111",member.get().getPassword());
                //일단 세션은 부여 -> 소셜로그인이라도 회원정보는 먼저 디비에 들어가있기때문에.
                //로그가 같다고 다 찍히는 건 아님.
                //로그인 유효성 검사.
                if(member.get().getAddress()==null && fromSocial){
                    //첫번째 로그인이다. 회원가입하고 처음 정보 들어옴.
                    //회원정보 수정페이지로 넘겨주어야한다.
                    request.getSession().setAttribute("memberDTO",authMember);
                    redirectStratgy.sendRedirect(request, response, "/member/firstUpdate");
                    System.out.println("여기까지인가?");
                } else if(passwordResult && fromSocial){
                    //로그인 성공이면서 여기는 2번째 이후부터 접속한사람.
                    log.info("여기는 2번째이후 접속.");
                    redirectStratgy.sendRedirect(request, response, "/shopping/main");//수정페이지로 가라
                }
            }else{
                //일반로그인인 경우
                System.out.println("여기는 일반회원인 경우 세션만 생성.");
                redirectStratgy.sendRedirect(request, response, "/shopping/main");//수정페이지로 가라
            }
        }else{
            System.out.println("해당 아이디가 없네요........이상하네?");
        }
    }

}