package com.ohrray.service;

import com.ohrray.domain.CartDTO;
import com.ohrray.domain.MemberDTO;
import com.ohrray.domain.AddCartDTO;
import com.ohrray.domain.AddPaymentDTO;

import java.util.List;
import java.util.Map;

public interface CartService{

    public Long addCart(AddCartDTO addcartDTO, String email);

    public List<CartDTO> cartList(String email);

    public void updateCartProductCount(Long cartProductId, int productCount);

    public void deleteCartProduct(Long cartProductId);

    public int checkProductCount(Long cartProductId);

    // 상품별 합계
    public Map<Long, Integer> productTotal(List<CartDTO> cartLists);

    public MemberDTO getMember(AddPaymentDTO paymentDTO);
    // 전체 총계
    public int subTotal(List<CartDTO> cartLists);

    public List<String> findSize(Long productId);

    public List<String> findColor(Long productId);
}
