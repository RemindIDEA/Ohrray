package com.ohrray.domain;


import com.ohrray.entity.Product;
import com.ohrray.entity.ProductImg;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long pno; //상품코드
    private String productName; //상품명
    private int productPrice; //상품가격
    private int readCount; //조회수

    private ProductImgDTO productImgDTO = new ProductImgDTO();
    private Long productImgId;

    private LocalDateTime regDate; //등록일
    private LocalDateTime modDate; //수정일

    public void setData(String productName , int productPrice ,int readCount , ProductImgDTO productImgDTO){

        this.productName = productName;
        this.productPrice = productPrice;
        this.readCount = readCount;
        this.productImgDTO = productImgDTO;

    }

    public ProductDTO(Product product , ProductImg productImg){
        this.pno=product.getId();
        this.productName = product.getProductName();
        this.productPrice = product.getProductPrice();
        this.readCount = product.getReadCount();
        this.productImgDTO = new ProductImgDTO();
        this.productImgDTO.setId(productImg.getId());
        this.productImgDTO.setMainImgName(Collections.singletonList(productImg.getMainImg1()));
    }

}
