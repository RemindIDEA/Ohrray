package com.ohrray.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategotyDTO {

    private Long pid;
    private String mainCategory;
    private String subCategory;

    public void setData(String mainCategory, String subCategory){
        this.mainCategory = mainCategory;
        this.subCategory =subCategory;
    }

}
