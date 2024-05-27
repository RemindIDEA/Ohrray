package com.ohrray.service;

import com.ohrray.domain.MemberDTO;
import com.ohrray.entity.Member;
import com.ohrray.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@NoArgsConstructor
public class MemberServiceImpl implements MemberService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    //중복검사 시 사용.
    @Override
    public boolean validateMember(String email) {
        //id는 의미없는 숫자로  email 을 id 처럼 사용해야함.
        Optional<Member> validateResult = memberRepository.findByEmail(email);
        //회원이 있는 경우 false 처리
        if(validateResult.isPresent()){
            System.out.println("duple----------------");
            return false;
        } else{
            //회원이 없는 경우 true 처리
            System.out.println("none---------------------");
            return true;
        }
    }

    @Override
    public MemberDTO getMember(MemberDTO memberDTO) {
        Member member = memberRepository.findByEmail(memberDTO.getEmail()).orElseThrow(EntityNotFoundException::new);
        return memberDTO.changeToDTO(member);
    }

    //회원가입
    @Transactional
    @Override
    public void join(MemberDTO memberDTO) {
        Member member = memberDTO.changeToEntity(memberDTO);
        //비밀번호 암호화
        member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        Member save = memberRepository.save(member);
        System.out.println("잘 저장됐는지 체크 save.getNickName() = " + save.getNickName());
    }

    @Transactional
    @Override
    public void updateMember(MemberDTO memberDTO) {
        Member member = memberRepository.findByEmail(memberDTO.getEmail()).orElseThrow(EntityNotFoundException::new);
        if(member.isSocial()){
            //소셜 로그인 아이디일 경우
            member.setNickName(memberDTO.getNickName());
            member.setPhone(memberDTO.getPhone());
            member.setAddress(memberDTO.getAddress());
            member.setAddressDetail(memberDTO.getAddressDetail());
        }else{
            //일반 회원일 경우
            //비밀번호 인코딩해서 작업.
            member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
            member.setNickName(memberDTO.getNickName());
            member.setPhone(memberDTO.getPhone());
            member.setAddress(memberDTO.getAddress());
            member.setAddressDetail(memberDTO.getAddressDetail());
        }
    }

    @Transactional
    @Override
    public boolean deleteMember(MemberDTO memberDTO) {
        //아이디 잘못 입력해도 오류 발생
        Member findMember = memberRepository.findByEmail(memberDTO.getEmail()).orElseThrow(EntityNotFoundException::new);
        boolean matches = passwordEncoder.matches(memberDTO.getPassword(), findMember.getPassword());
        System.out.println("matches = " + matches);
        System.out.println("입력한 비밀번호가 맞아야 삭제가능.");
        if(matches){
            System.out.println("여기는 비밀번호가 맞아서 삭제하는곳");
            memberRepository.deleteById(findMember.getId());
            return true;
        }else{
            System.out.println("비밀번호 잘못입력해서 안됨.");
            return false;
        }
    }

    //소셜 로그인했을때 정보를 받기위해 처음에만 받기.
    @Transactional
    @Override
    public void updateFirst(MemberDTO memberDTO) {
        Member member = memberRepository.findByEmail(memberDTO.getEmail()).orElseThrow(EntityNotFoundException::new);
        member.setNickName(memberDTO.getNickName());
        member.setPhone(memberDTO.getPhone());
        member.setAddress(memberDTO.getAddress());
        member.setAddressDetail(memberDTO.getAddressDetail());
    }

    @Override
    public MemberDTO getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();
        MemberDTO memberDTO = new MemberDTO().changeToDTO(member);
        return memberDTO;
    }

}
