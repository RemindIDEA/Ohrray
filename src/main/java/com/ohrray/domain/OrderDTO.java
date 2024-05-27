package com.ohrray.domain;

import com.ohrray.enums.PayStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    //오더번호
    private Long id;
    //주문자
    private MemberDTO member;
    //주문상세정보
    private List<OrderDetailDTO> orderDetailDTO = new ArrayList<OrderDetailDTO>();
    //주문주소 + 상세주소
    private String orderAddress;
    //수령인
    private String recipient;
    //총결제액
    private int totalPrice;
    //결제 영수증 번호(전체 환불할 경우)
    private String receiptId;

    //결제 상태
    @Enumerated(EnumType.STRING)
    private PayStatus paymentStatus;
}
