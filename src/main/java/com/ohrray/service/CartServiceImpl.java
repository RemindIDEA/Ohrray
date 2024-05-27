package com.ohrray.service;

import com.ohrray.domain.*;
import com.ohrray.entity.Cart;
import com.ohrray.entity.Member;
import com.ohrray.entity.Option;
import com.ohrray.entity.Product;
import com.ohrray.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Log4j2
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OptionRepository optionsRepository;
    private final ProductImgRepository productImgRepository;

    @Transactional
    @Override
    public Long addCart(AddCartDTO cartDTO, String email) {

        // 상품 조회. 상품이 없으면 예외처리
        Product product = productRepository.findById(cartDTO.getPno())
                .orElseThrow(() -> new EntityNotFoundException("상품 없음"));
        System.out.println("product.getProductImg().getMainImg1() = " + product.getProductImg().getMainImg1());
        // 회원 조회. 계정 없으면 예외처리

        Member member = memberRepository.findByEmail(email).orElseThrow(()
                ->new EntityNotFoundException("계정 없음"));
        
        Option productOption = optionsRepository.findById(cartDTO.getOno()).orElseThrow(EntityNotFoundException::new);

        Cart cart1 = Cart.builder()
                .product(product)
                .member(member)
                .productCount(cartDTO.getCount())
                .option(productOption)
                .build();
        cartRepository.save(cart1);

        return null;
    }


    @Override
    public List<CartDTO> cartList(String email) {
        //장바구니 조회를 위한 멤버 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        List<CartDTO> list = cartRepository.findByMemberEmail(member.getEmail())
                .stream().map(cart ->
                        CartDTO.builder()
                                .id(cart.getId())
                                .member(new MemberDTO().changeToDTO(cart.getMember()))
                                .product(new ProductDTO(cart.getProduct(),cart.getProduct().getProductImg()))
                                .productCount(cart.getProductCount())
                                .option(new ProductOptionDTO().setData(cart.getOption().getSize(), cart.getOption().getColor(),cart.getOption().getStock(),cart.getOption().getFullOption()))
                                .build()
                ).toList();

        return list;

    }


    // 장바구니 상품 수량 수정
    @Transactional
    @Override
    public void updateCartProductCount(Long cartProductId, int productCount) {
        Cart cart=cartRepository.findById(cartProductId)
                .orElseThrow(EntityNotFoundException::new);
        cart.setProductCount(cart.updateCount(productCount));
    }

    // 장바구니 상품 삭제
    @Transactional
    @Override
    public void deleteCartProduct(Long cartProductId) {

        cartRepository.deleteById(cartProductId);
    }

    @Override
    public int checkProductCount(Long cartProductId) {

        return cartRepository.findByCartId(cartProductId);
    }

    // 상품별 합계
    @Transactional
    @Override
    public Map<Long, Integer> productTotal(List<CartDTO> cartLists) {
        Map<Long, Integer> productTotal = new HashMap<>();
        for(CartDTO cartDTO : cartLists) {
            int total = cartDTO.getProduct().getProductPrice() * cartDTO.getProductCount();
            System.out.println("total = " + total);
            productTotal.put(cartDTO.getId(), total);
        }
        return productTotal;
    }

    @Override
    public MemberDTO getMember(AddPaymentDTO paymentDTO) {

        Cart cart = cartRepository.findById(paymentDTO.getCartId().get(0)).orElseThrow();
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setNickName(cart.getMember().getNickName());
        memberDTO.setAddress(cart.getMember().getAddress());
        memberDTO.setAddressDetail(cart.getMember().getAddressDetail());
        memberDTO.setPhone(cart.getMember().getPhone());
        return memberDTO;
    }

    // 전체 총계
    @Transactional
    @Override
    public int subTotal(List<CartDTO> cartLists) {
        int subTotal=0;
        for(CartDTO cartDTO : cartLists){
            subTotal+= cartDTO.getProductCount()* cartDTO.getProduct().getProductPrice();
        }
        return subTotal;
    }

    @Override
    public List<String> findSize(Long productId) {
        return optionsRepository.findSizesByProductId(productId);
    }

    @Override
    public List<String> findColor(Long productId) {
        return optionsRepository.findColorsByProductId(productId);
    }


//    // 장바구니 상품 옵션 변경 (수정 중) (수정 중) (수정 중) (수정 중) (수정 중) (수정 중) (수정 중)
//    @Override
//    @Transactional
//    public List<Options> findOptionsByCartIdAndProductId(Long cartId, Long productId) {
//        // 우선, 장바구니 ID를 통해서 장바구니 엔티티 조회
//        Cart cart = cartRepository.findById(cartId).orElseThrow(
//                () -> new NoSuchElementException("해당 ID의 장바구니가 없음"));
//
//        // 장바구니와 연관된 상품이 입력된 상품 ID와 일치하는 경우, 해당 상품의 옵션들을 조회
//        if (cart.getProduct().getId().equals(productId)) {
//            return optionsRepository.findByProductId(productId);
//        } else {
//            throw new IllegalStateException("상품이 장바구니에 없음");
//        }
//    }
}
