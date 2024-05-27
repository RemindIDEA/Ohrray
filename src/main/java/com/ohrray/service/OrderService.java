package com.ohrray.service;

import com.ohrray.domain.OrderDetailDTO;

import java.util.List;

public interface OrderService {

    public List<OrderDetailDTO> getOrderDetail(String email);
}
