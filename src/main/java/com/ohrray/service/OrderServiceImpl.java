package com.ohrray.service;

import com.ohrray.domain.OrderDetailDTO;
import com.ohrray.entity.Member;
import com.ohrray.entity.OrderDetail;
import com.ohrray.entity.Orders;
import com.ohrray.repository.MemberRepository;
import com.ohrray.repository.OrderDetailRepository;
import com.ohrray.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    @Override
    public List<OrderDetailDTO> getOrderDetail(String email) {
        System.out.println("여기는 서비스 영역 email = " + email);
        //email로 member가져오기
        Member member = memberRepository.findByEmail(email).orElseThrow();
        System.out.println("member.getEmail() = " + member.getEmail());
        //member로 order가져오기(오더번호)
        List<Orders> orders = orderRepository.findByMember(member);
        List<OrderDetailDTO> detailList = new ArrayList<>();
        //order번호로 orderDetailList 가져오기
        for (Orders order : orders) {
            List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(order.getId());
            for (OrderDetail orderDetail : orderDetailList) {
                //생성자를 이용한 DTO변경
                OrderDetailDTO dto = new OrderDetailDTO(orderDetail);
                detailList.add(dto);
            }
        }
        for(OrderDetailDTO detail :detailList){
            System.out.println("detail = " + detail.toString());
        }

        return detailList;
    }
}
