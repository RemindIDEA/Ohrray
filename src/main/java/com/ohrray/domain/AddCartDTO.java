package com.ohrray.domain;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class AddCartDTO {

    private Long id;
    private Long mid;
    private Long pno;
    private int count;
    private Long ono;
}
