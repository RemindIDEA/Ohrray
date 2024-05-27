package com.ohrray.domain;


import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductFormDTO {

    private Long pno; //상품코드
    private Long mid; //작성자
    private String productName; //상품명
    private int productPrice; //상품가격
    private int readCount; //조회수
    private LocalDateTime regDate; //등록일
    private LocalDateTime modDate; //수정일
    //productDTO


    private Long productImgId;
    @Builder.Default
    private List<MultipartFile> mainImg = new ArrayList<>(); //메인이미지 파일
    @Builder.Default
    private List<String> mainImgName = new ArrayList<>();
    private List<MultipartFile> detailImg =new ArrayList<>(); //상세보기 이미지 파일
    private List<String> detailImgName = new ArrayList<>();

    private String PrimaryImg;
    //productImgDTO

    private Long cno; //카테고리 번호
    private String mainCategory;//대분류
    private String subCategory;//소분류
    //productCategoryDTO

    private List<ProductOptionDTO> option = new ArrayList<>();//상품옵션


    //ProducyOptionDTO

    //Querydsl로 결과 조회 시 ProductFormDTO 객체로 바로 받아오도록 활용
    @QueryProjection
    public ProductFormDTO (Long pno ,String productName, String primaryImg ,int productPrice,String mainCategory){
        this.pno=pno;
        this.productName=productName;
        this.PrimaryImg=primaryImg;
        this.productPrice=productPrice;
        this.mainCategory=mainCategory;
    }



}
