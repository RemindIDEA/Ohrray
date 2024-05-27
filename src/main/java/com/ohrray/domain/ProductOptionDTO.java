package com.ohrray.domain;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionDTO {

    private Long ono;

    private String color;

    private String size;

    private Long stock;

    private String fullOption;


    public ProductOptionDTO setData(String color , String size , Long stock , String fullOption){

        this.color=color;
        this.size =size;
        this.stock = stock;
        this.fullOption =fullOption;
        return this;
    }



}
