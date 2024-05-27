package com.ohrray.domain;

import com.ohrray.entity.OrderDetail;
import com.ohrray.enums.DetailStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    //오더상세정보
    private Long orderDetailId;
    //오더 번호
    private Long orderId;
    //주문상품
    private ProductDTO product;

    //상품구매가격
    private int productPriceNow;

    //주문 갯수
    private int count;

    //배송 상태
    @Enumerated(EnumType.STRING)
    private DetailStatus detailStatus;

    private boolean isReview;

    //주문시간
    private LocalDateTime regDate;
    //수정시간
    private LocalDateTime modDate;

    public OrderDetailDTO(OrderDetail orderDetail) {
        this.orderDetailId = orderDetail.getId();
        this.orderId =orderDetail.getOrders().getId();
        this.product= new ProductDTO(orderDetail.getProduct(), orderDetail.getProduct().getProductImg());
        this.productPriceNow =orderDetail.getProductPriceNow();
        this.count = orderDetail.getCount();
        this.detailStatus = orderDetail.getDetailStatus();
        this.regDate = orderDetail.getRegDate();
        this.modDate = orderDetail.getModDate();
        this.isReview = orderDetail.isReview();
    }
}
