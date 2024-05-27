package com.ohrray.domain;


import com.ohrray.entity.Option;
import com.ohrray.entity.Product;
import com.ohrray.entity.ProductImg;
import lombok.*;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;
@Builder
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor

public class CartDTO {
    //cart id
    private Long id;

    //cart 쓰는 회원
    private MemberDTO member;

    //게시글 정보
    private ProductDTO product;

    //상품갯수
    private int productCount;

    // 상품 옵션
    private ProductOptionDTO option;


}

