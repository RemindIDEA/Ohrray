package com.ohrray.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Product extends BaseEntity{
    //상품게시물 ok
    @Id @GeneratedValue
    @Column(name = "PRODUCT_ID")
    private Long id;

    //회원정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    //상품세부 주문정보들
    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    //카테고리
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_CATEGORY_ID")
    private ProductCategory productCategory;

    //상품명
    private String productName;
    //가격
    private int productPrice;
    // 옵션
    @OneToMany(mappedBy = "product")
    private List<Option> options = new ArrayList<>();

    //본문
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCTIMG_ID")
    private ProductImg productImg;

    //조회수
    private int readCount;



    //등록일
    //수정일
}
