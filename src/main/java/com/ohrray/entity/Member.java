package com.ohrray.entity;

import com.ohrray.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
//@SQLDelete(sql = "UPDATE MEMBER SET deleted_at = current_timestamp WHERE id = ?")
//@Where(clause = "deleted_at is null")
public class Member {
    //pk로 자동 1씩 증가 되는 수 설정.
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
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

    //회원 권한 -> 일반유저, 판매자, 관리자 컬렉션으로 변경
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Set<Role> roleSet = new HashSet<>();

    //상속으로 가입시간 받기.
    //변경 시간 받기

    public void addMemberRole(Role role){
        roleSet.add(role);
    }
}
