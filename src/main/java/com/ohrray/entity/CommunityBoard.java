package com.ohrray.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityBoard extends BaseEntity{
    @Id @GeneratedValue
    @Column(name="COMMUNITY_BOARD_ID")
    private Long id;

    //커뮤니티 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMMUNITY_ID")
    private Community community;

    //작성자 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    //이미지
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="COMMUNITY_IMG_ID")
    private CommunityImg communityImg;

    //글 내용
    private String title;
    private String content;

    //등록일, 수정일
}
