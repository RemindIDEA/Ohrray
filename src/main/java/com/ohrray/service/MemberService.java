package com.ohrray.service;

import com.ohrray.domain.MemberDTO;

public interface MemberService {
    public boolean validateMember(String email);
    //회원정보 가져오기
    public MemberDTO getMember(MemberDTO memberDTO);
    //회원가입
    public void join(MemberDTO memberDTO);
    //회원정보수정
    public void updateMember(MemberDTO memberDTO);
    //회원 탈퇴
    public boolean deleteMember(MemberDTO memberDTO);
    //소셜로그인 처음 정보 수정하기
    public void updateFirst(MemberDTO memberDTO);

    public MemberDTO getMemberByEmail(String email);
}
