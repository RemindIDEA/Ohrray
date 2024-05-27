package com.ohrray.entity;

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
public class ProductCategory {

    //상품게시물 ok
    @Id @GeneratedValue
    @Column(name = "PRODUCT_CATEGORY_ID")
    private Long id;

    //대분류
    private String mainCategory;

    //소분류
    private String subCategory;


}
