package com.ohrray.domain;

import com.ohrray.entity.Product;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Setter @Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionDTO {
    private Long id;

    //사이즈
    private String size;

    //색상
    private String color;

    //재고
    private Long stock;

    private String fullOption;

    private Product product;



}
