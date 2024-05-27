package com.ohrray.security.service;

import com.ohrray.domain.SecurityMemberDTO;
import com.ohrray.entity.Member;
import com.ohrray.enums.Role;
import com.ohrray.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class OAuthUserDetailsService extends DefaultOAuth2UserService {
    //소셜 로그인용 !! 로그인한 유저의 정보를 가져오기위해서 필요한게 이거임
    // 소셜 로그인을 하는 순간 여기가 들어와서 정보를 먼저 저장하고 기존에 있으면 그냥 끝내고
    // 그다음에 핸들러를 통해서 처음에는 정보를 받아올수 있게.
    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;
    String company = "notsosical";
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        //인증을 요청할 때 loadUser()가 실행된다.
        System.out.println("여기는 소셜 로그인시 들어오는 공간입니다.");
        System.out.println("userRequest = " + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName(); // 서비스회사 ex 구글, 네이버
        //첫번째 -서비스회사 확인
        log.info("서비스회사: " + clientName);
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User =super.loadUser(userRequest);
        //두번째 -부모 클래스인 DefaultOAuth2UserService 의 loadUser()실행 -> OAuth2 프로바이더에서 사용자 정보를 가져오는 기본적인 동작을 수행한다.
        // 즉 , 소셜 로그인을 통해 인증되면 해당 사용자의 정보를 가져와서 OAuth2User 객체로 반환

        oAuth2User.getAttributes().forEach((k,v) -> {
            System.out.println("k = " + k);
            System.out.println("v = " + v);
        });

        String email = "";

        if(clientName.equals("Google")){
            email = oAuth2User.getAttribute("email");
            company ="google";
        }else if(clientName.equals("Kakao")){
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            company ="kakao";
            email = (String) kakaoAccount.get("email");
        }else if(clientName.equals("Naver")){
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("response");
            //이런식으로 가져와야 네이버의 이메일 가져옴
            company ="naver";
            email = (String) kakaoAccount.get("email");
        };

        Member member = saveSocialMember(email);
        System.out.println("가져온 메일로 member 체크하기 = " + member);
        SecurityMemberDTO authMember = new SecurityMemberDTO(
                member.getEmail(),
                member.getPassword(),
                member.isSocial(),
                member.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList()),
                oAuth2User.getAttributes()
                );
        return authMember;
    }

    private Member saveSocialMember(String email){
        //기존에 동일한 이메일로 가입한 회원이 있는 경우에는 그대로 조회만
        Optional<Member> result = repository.findByEmail(email);//0411수정
        if(result.isPresent()){
            System.out.println("이미 아이디가 있네요");
            return result.get();
        }
        System.out.println("없으니까 새로 만들어서 정보 넣어볼게요.");
        //없다면 회원 추가 패스워드는 1111 이름은 그냥 이메일 주소로
        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode("1111"))
                .social(true)
                .build();
        member.addMemberRole(Role.USER);
        Member save = repository.save(member);
        return save;
    }
}
