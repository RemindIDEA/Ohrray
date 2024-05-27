package com.ohrray.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    //오더 아이디
    private Long orderId;

    //멤버 아이디
    private String email;
    //주소
    private String orderAddress;
    //수령인
    private String recipient;
    //총 결제금액
    private int totalPrice;

    //결제상태
    private String paymentStatus;
    //결제 영수증번호
    private String receiptId;


}
