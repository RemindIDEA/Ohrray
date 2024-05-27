package com.ohrray.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "OPTIONS")
public class Option {

    //상품 사이즈 pk;
    @Id @GeneratedValue
    @Column(name = "OPTIONS_ID")
    private Long id;


    //사이즈
    private String size;

    //색상
    private String color;

    //재고
    private Long stock;

    private String fullOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

}
