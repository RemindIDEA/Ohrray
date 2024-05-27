package com.ohrray.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddPaymentDTO {

    private List<Long> cartId =new ArrayList<>();
}
