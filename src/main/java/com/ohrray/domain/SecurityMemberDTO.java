package com.ohrray.domain;

import com.ohrray.entity.Member;
import com.ohrray.enums.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.*;

@Setter
@Getter
public class SecurityMemberDTO extends User implements OAuth2User {
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

    private Map<String, Object> attr;


    public SecurityMemberDTO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email=username;
        this.password = password;
    }

    public SecurityMemberDTO(String username, String password, boolean isSocial , Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email=username;
        this.password = password;
        this.social = isSocial;
    }
    public SecurityMemberDTO(String username, String password, boolean isSocial , Collection<? extends GrantedAuthority> authorities, Map<String, Object> attr) {
        super(username, password, authorities);
        this.email=username;
        this.password = password;
        this.social = isSocial;
        this.attr = attr;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }

    @Override
    public String getName() {
        return this.getNickName();
    }

    public  Member changeToEntity(SecurityMemberDTO securityMemberDTO){
        Member member = new Member();
        member.setEmail(securityMemberDTO.getEmail());
        member.setPassword(securityMemberDTO.getPassword());
        member.setPhone(securityMemberDTO.getPhone());
        member.setNickName(securityMemberDTO.getNickName());
        member.setAddress(securityMemberDTO.getAddress());
        member.setAddressDetail(securityMemberDTO.getAddressDetail());
        member.setSocial(false);
        member.addMemberRole(Role.USER);

        return member;
    }

    public SecurityMemberDTO changeToDTO(Member member){
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
