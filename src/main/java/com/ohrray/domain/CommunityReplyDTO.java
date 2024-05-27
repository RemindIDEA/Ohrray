package com.ohrray.domain;

import com.ohrray.entity.CommunityBoard;
import com.ohrray.entity.CommunityReply;
import com.ohrray.entity.Member;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityReplyDTO {
    private Long id;
    private Long bid;
    private Long mid;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public CommunityReplyDTO entityToDto(CommunityReply communityReply){
        CommunityReplyDTO communityReplyDTO = new CommunityReplyDTO();
        this.id = communityReply.getId();
        this.bid = communityReplyDTO.getBid();
        this.mid=communityReply.getMember().getId();
        this.content=communityReply.getContent();
        this.regDate = communityReply.getRegDate();
        this.modDate = communityReply.getModDate();
        return this;
    }
    public CommunityReply addEntity(CommunityReplyDTO communityReplyDTO, CommunityBoard communityBoard,Member member){
        CommunityReply reply = CommunityReply.builder()
                .communityBoard(communityBoard)
                .member(member)
                .content(communityReplyDTO.getContent())
                .build();
        return reply;
    }

}
