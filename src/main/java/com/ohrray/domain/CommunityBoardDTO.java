package com.ohrray.domain;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityBoardDTO {
    //Pk
    private Long bid;

    //커뮤니티정보
    private Long cid;

    //아이디 정보
    private Long mid;
    //파일명
    private List<String> fileName = new ArrayList<>();
    //사진 파일
    private List<MultipartFile> imgFiles = new ArrayList<>();

    //제목
    private String title;
    //내용
    private String content;
    //작성시간
    private LocalDateTime regDate;
    //수정시간
    private LocalDateTime modDate;

    public CommunityBoardDTO(Long bid, Long cid, List<String> fileName, String title, String content ){
        this.bid = bid;
        this.cid = cid;
        this.fileName = fileName;
        this.title = title;
        this.content = content;
    }
}
