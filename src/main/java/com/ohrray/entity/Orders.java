package com.ohrray.entity;

import com.ohrray.enums.PayStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders extends BaseEntity {
    //주문정보 pk
    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    //주문자 정보
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    @Builder.Default // Builder를 사용하여 초기화
    @OneToMany(mappedBy = "orders")
    private List<OrderDetail> orderDetailList = new ArrayList<>();

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




    //등록 시간(주문 날짜)
    //수정 시간
    public void plusOrderDetail(OrderDetail orderDetail){
        orderDetailList.add(orderDetail);
    }
}
