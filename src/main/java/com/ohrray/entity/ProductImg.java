package com.ohrray.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImg {
    //상품게시물 ok
    @Id @GeneratedValue
    @Column(name = "PRODUCT_IMG_ID")
    private Long id;

    //메인이미지\
    @Column(nullable = false)
    private String mainImg1;
    private String mainImg2;
    private String mainImg3;

    //상세정보 이미지
    @Column(nullable = false)
    private String detailImg1;
    private String detailImg2;
    private String detailImg3;


}
