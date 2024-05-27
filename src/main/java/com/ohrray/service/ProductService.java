package com.ohrray.service;

import com.ohrray.domain.*;

import java.util.List;

public interface ProductService {

    public void registerProductSales(ProductFormDTO productFormDTO , String email) throws Exception;

    public PageResultDTO findSalesProduct(PageRequestDTO pageRequestDTO);

    public ProductFormDTO findOneProduct(Long pno);

    public ProductFormDTO updateProduct(ProductFormDTO productFormDTO) throws Exception;

    public void deleteProduct(Long pno);

//----------------------------------------------------------------------
    public void insertOrder(PaymentDTO paymentDTO, AddPaymentDTO addPaymentDTO);


}
