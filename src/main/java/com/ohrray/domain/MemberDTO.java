package com.ohrray.domain;

import com.ohrray.entity.Member;
import com.ohrray.enums.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {
    private Long id;
    //아이디
    private String email;
    //비밀번호
    private String password;
    //닉네임
    private String nickName;
    //전화번호
    private String phone;
    //주소
    private String address;
    //상세주소
    private String addressDetail;

    //소셜로그인 여부
    private boolean social;

    private Set<Role> role = new HashSet<>();

    private LocalDateTime regDate ;
    private LocalDateTime modDate;


    public Member changeToEntity(MemberDTO memberDTO){
        Member member = new Member();
        member.setEmail(memberDTO.getEmail());
        member.setPassword(memberDTO.getPassword());
        member.setPhone(memberDTO.getPhone());
        member.setNickName(memberDTO.getNickName());
        member.setAddress(memberDTO.getAddress());
        member.setAddressDetail(memberDTO.getAddressDetail());
        member.setSocial(false);
        member.addMemberRole(Role.USER);

        return member;
    }

    public MemberDTO changeToDTO(Member member){
        this.id = member.getId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.nickName = member.getNickName();
        this.phone = member.getPhone();
        this.address = member.getAddress();
        this.addressDetail = member.getAddressDetail();
        this.social = member.isSocial();
        this.role = member.getRoleSet();
        return this;
    }
}
