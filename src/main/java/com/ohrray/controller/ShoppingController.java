package com.ohrray.controller;

import com.ohrray.domain.*;
import com.ohrray.service.CartService;
import com.ohrray.service.MemberService;
import com.ohrray.service.ProductService;
import com.ohrray.service.ReplyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@NoArgsConstructor
@RequestMapping("/shopping")
public class ShoppingController {

    @Autowired
    private ProductService productService;
    @Autowired
    private  CartService cartService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ReplyService replyService;

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/register")
    public void registerForm(){

    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/register")
    public String registerProductSales(ProductFormDTO productFormDTO ,HttpServletRequest request){
        HttpSession session =request.getSession();
        String email = (String)session.getAttribute("email");
        System.out.println("productFormDTO.getOption() = " + productFormDTO.getOption());
        
        try{
           productService.registerProductSales(productFormDTO ,email);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/shopping/main";

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping({"/main","/main?{size}/?{keyword}"})
    public void listOfProductsOnSale(Model model , PageRequestDTO pageRequestDTO){
        System.out.println("pageRequestDTO = " + pageRequestDTO.getSize());
        model.addAttribute("result",productService.findSalesProduct(pageRequestDTO));

        System.out.println("컨트롤러 끝");
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/read")
    public void oneOfProduct(Long pno ,int page, int size ,Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        String email = (String)session.getAttribute("email");
        model.addAttribute("product" , productService.findOneProduct(pno));
        model.addAttribute("page",page);
        model.addAttribute("size",size);
        model.addAttribute("member", memberService.getMemberByEmail(email));
    }
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/update")
    public void oneOfProduct2(Long pno , Model model){
        System.out.println("컨트롤러");
        model.addAttribute("product" , productService.findOneProduct(pno));

    }
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping("/update")
    public String updateProduct(ProductFormDTO productFormDTO)  {
        System.out.println("수정을 합시다 . 컨트롤러입니다");
        for(int i=0;i<productFormDTO.getOption().size(); i++){
            System.out.println("productFormDTO = " + productFormDTO.getOption().get(i).getColor());
        }

        try{
        productService.updateProduct(productFormDTO);
        }catch (Exception e){
            e.printStackTrace();
        }

        Long pno =productFormDTO.getPno();
        System.out.println("수정끗");
        return "redirect:/shopping/main";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/delete")
    public String deleteProduct(Long pno){
        System.out.println("삭제 컨트롤러");
        System.out.println(pno);
        productService.deleteProduct(pno);
        return "redirect:/shopping/main";
    }



    // 장바구니에 상품 담기
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/add")
    public String addCartProduct(@RequestParam("pno")Long pno,@RequestParam("size")int size,
                                 @RequestParam("page")int page, AddCartDTO cartDTO, Principal principal){
        System.out.println("pno는 " + cartDTO.getPno());
        System.out.println("cartDTO.getOption().toString() = " + cartDTO.getOno());
        System.out.println("cartDTO.getCount() = " + cartDTO.getCount());

        String email = principal.getName();
        System.out.println("장바구니사용자" + email);
        cartService.addCart(cartDTO, email);
        return  "redirect:/shopping/read?pno="+pno+"&size="+size+"&page="+page;
    }


    // 장바구니 상품목록 조회
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/cart")
    public void cartList(Model model,Principal principal) {
        String email = principal.getName();
        List<CartDTO> cartLists=cartService.cartList(email);

        //System.out.println(cartLists.get(0).getProduct().getProductImgDTO().getMainImgName());
        // 상품별 총계
        Map<Long, Integer> productTotal=cartService.productTotal(cartLists);
        // 전체 총계
        int subTotal=cartService.subTotal(cartLists);

        model.addAttribute("cartLists", cartLists);
        model.addAttribute("productTotal", productTotal);
        model.addAttribute("subTotal", subTotal);
    }

    // 장바구니 수량 변경
    @PutMapping(value = "/cartProduct/{id}/{qty}")
    @ResponseBody
    public ResponseEntity<String> updateCartProduct(@PathVariable("id") Long id,@PathVariable("qty") int qty){
        System.out.println("id = " + id);
        System.out.println("qty = " + qty);
        if(qty == -1){
            int count = cartService.checkProductCount(id);
            System.out.println("count = " + count);
            if(count <= 1){
                return new ResponseEntity<>("중복된 ID입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        cartService.updateCartProductCount(id, qty);
        return new ResponseEntity<String>("성공", HttpStatus.OK);
    }

    // 장바구니 상품 삭제
    @DeleteMapping(value = "/cartProduct/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCartProduct(@PathVariable ("id") Long id){
        System.out.println("cartProductId = " + id);
        
        cartService.deleteCartProduct(id);
        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }


    @GetMapping("/payRegister")
    public void goPay(AddPaymentDTO addpaymentDTO, @RequestParam("totalPrice") String totalPrice, Model model ){
        System.out.println("totalPrice = " + totalPrice);
        for(Long id : addpaymentDTO.getCartId()){
            System.out.println("id = " + id);
        }
        MemberDTO member = cartService.getMember(addpaymentDTO);
        model.addAttribute("addPaymentDTO", addpaymentDTO);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("member", member);
    }




    @PostMapping("/orders")
    public String insertOrder(PaymentDTO paymentDTO, AddPaymentDTO addPaymentDTO){
        System.out.println("paymentDTO.getOrderAddress() = " + paymentDTO.getOrderAddress());
        System.out.println("paymentDTO.getRecipient() = " + paymentDTO.getRecipient());
        System.out.println("paymentDTO.getTotalPrice() = " + paymentDTO.getTotalPrice());
        System.out.println("paymentDTO.getReceiptId() = " + paymentDTO.getReceiptId());
        System.out.println("paymentDTO = " + paymentDTO.getPaymentStatus());
        for(Long id : addPaymentDTO.getCartId()){
            System.out.println("id = " + id);
        }
        productService.insertOrder(paymentDTO,addPaymentDTO);

        return "redirect:/shopping/main";
    }



    /* 리뷰부분-------------------------------------------------------------------------*/
    @GetMapping ("/addReview")
    public void addReviewForm(@RequestParam("pno")Long pno,@RequestParam("orderDetailId")Long orderDetailId, Model model, HttpServletRequest request){
        System.out.println("pno = " + pno);
        ProductFormDTO oneProduct = productService.findOneProduct(pno);
        //세션을통한 작성자 구하지
        HttpSession session = request.getSession();
        String email = (String)session.getAttribute("email");
        MemberDTO member = memberService.getMemberByEmail(email);
        model.addAttribute("product",oneProduct);
        model.addAttribute("member", member);
        model.addAttribute("orderDetailId",orderDetailId);
    }


    @PostMapping("/addReview")
    public String addReview(ProductReviewDTO productReviewDTO ,@RequestParam("orderDetailId") Long orderDetailId) {
        System.out.println("컨트롤러부분 productReviewDTO.getReview() = " + productReviewDTO.getReview());
        System.out.println("orderDetailId = " + orderDetailId);
        ProductReviewDTO result = replyService.addReview(productReviewDTO, orderDetailId);
       return "redirect:/member/myPage";
    }

    /* 리뷰부분-------------------------------------------------------------------------*/


}
