package com.ohrray.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "CART_ID")
    private Long id;

    //회원정보
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    //게시글 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    //상품갯수
    private int productCount;

    // 상품 옵션
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPTIONS_ID")
    private Option option;


    public void changeOptions(Option options){
        this.option=option;
    }


    public void changeMember(Member member){this.member=member;}



    // 장바구니 생성
    public static Cart createCart(Member member){
        Cart cart=new Cart();
        cart.changeMember(member);
        return cart;
    }

    public void changeCart(Long id){
        this.id=id;
    }

    public void changeProduct(Product product){
        this.product=product;
    }

    public void changeProductCount(int productCount){
        this.productCount+=productCount;
    }


    // 카트에 담을 상품 생성
    public static Cart createCartProduct(Cart cart, Product product, int count){
        Cart cartProduct=new Cart();
        cartProduct.changeCart(cart.getId());
        cartProduct.changeProduct(product);
        cartProduct.changeProductCount(count);
        return cartProduct;
    }

    // 기존 상품을 추가로 담을 때
    public void addCount(int productCount){
        this.productCount += productCount;
    }

    // 장바구니 상품 수량 변경
    public int updateCount(int productCount){
        this.productCount+=productCount;
        return this.productCount;
    }

}