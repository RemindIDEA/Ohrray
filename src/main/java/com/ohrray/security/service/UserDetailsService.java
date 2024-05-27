package com.ohrray.security.service;

import com.ohrray.domain.SecurityMemberDTO;
import com.ohrray.entity.Member;
import com.ohrray.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    //일반 로그인 했을때 여기를 콜백함수처럼 방문
    //success handler랑 별개로 여기를 무조건들림-> 정보를 principal 로 저장할 수 있다.
    private final MemberRepository memberRepository;
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername area==================================");

        Optional<Member> findResult = memberRepository.findByEmailandSocial(username, false);

        if(findResult.isEmpty()){
            throw new UsernameNotFoundException("가입하지 않거나 소셜로그인으로 가입한 경우 ");
        }

        Member member = findResult.get();

        //일반로그인시 회원 정보 가져와서 전부 집어넣고 principal에 저장할 내용 저장하기.
        SecurityMemberDTO securityMemberDTO = new SecurityMemberDTO(
                member.getEmail(),
                member.getPassword(),
                member.isSocial(),
                member.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                        .collect(Collectors.toSet())
        );
        securityMemberDTO.setNickName(member.getNickName());
        securityMemberDTO.setAddress(member.getAddress());
        securityMemberDTO.setAddressDetail(member.getAddressDetail());
        return securityMemberDTO;
    }
}
