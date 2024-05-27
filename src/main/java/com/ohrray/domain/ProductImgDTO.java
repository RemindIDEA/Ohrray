package com.ohrray.domain;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImgDTO {

    //pk
    private Long id;

    //메인이미지 파일명
    private List<String> mainImgName;

    //상세정보 이미지 파일명
    private List<String> detailNameImg;

    //경로
    private String path;

    // 파일타입의 이미지들
    private List<MultipartFile> mainImg;

    private List<MultipartFile> detailImg;

    public void setData(List<MultipartFile> mainImg, List<MultipartFile> detailImg){
        this.mainImg = mainImg;

        this.detailImg = detailImg;
    }

}
