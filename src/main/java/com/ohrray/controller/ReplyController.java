package com.ohrray.controller;

import com.ohrray.domain.CommunityReplyDTO;
import com.ohrray.domain.ProductQnADTO;
import com.ohrray.domain.ProductReviewDTO;
import com.ohrray.service.ReplyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/community/add")
    public ResponseEntity<CommunityReplyDTO> addReply(CommunityReplyDTO communityReplyDTO, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        System.out.println("등록하는 email은~~~~~ " + email);
        CommunityReplyDTO result = replyService.addReply(communityReplyDTO,email);
        System.out.println("등록된 댓글은?" + result);
        if (result == null)
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        else
            return new ResponseEntity<>(result, HttpStatus.OK);
    }
    //댓글 목록보기
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/community/getList")
    public ResponseEntity<List<CommunityReplyDTO>> getList(@RequestParam("bid") Long bid){
        System.out.println("bid 시불 = " + bid);
        List<CommunityReplyDTO> list = replyService.getList(bid);
        System.out.println("list.get(0).getContent() = " + list.get(0).getContent());
        if (list.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.OK);
        else
            return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //상세보기
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/community/get")
    public ResponseEntity<CommunityReplyDTO> get(CommunityReplyDTO communityReplyDTO){
        return new ResponseEntity<>(replyService.get(communityReplyDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/community/update")
    public ResponseEntity<CommunityReplyDTO> updateReply(CommunityReplyDTO communityReplyDTO) {
        CommunityReplyDTO result = replyService.updateReply(communityReplyDTO);
        if (result == null)
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        else
            return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @DeleteMapping("/community/delete")
    public ResponseEntity<String> deleteReply(CommunityReplyDTO communityReplyDTO) {
        System.out.println("communityReplyDTO.getId() = " + communityReplyDTO.getId());
        replyService.deleteReply(communityReplyDTO);
        return new ResponseEntity<>("삭제성공", HttpStatus.OK);
    }
//리뷰 ---------------------------------------------------------------------------------


    //댓글 목록보기
    @GetMapping("/shopping/getList")
    public ResponseEntity<List<ProductReviewDTO>> getReviewList(@RequestParam("pno") Long pno){
        System.out.println("bid = " + pno);
        List<ProductReviewDTO> list = replyService.getReviewList(pno);
        if (list.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.OK);
        else
            return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //상세보기
    @GetMapping("/shopping/get")
    public ResponseEntity<ProductReviewDTO> getReview(ProductReviewDTO productReviewDTO){
        return new ResponseEntity<>(replyService.get(productReviewDTO), HttpStatus.OK);
    }


    @PutMapping("/shopping/update")
    public ResponseEntity<ProductReviewDTO> updateReview(ProductReviewDTO productReviewDTO) {
        ProductReviewDTO result = replyService.updateReview(productReviewDTO);
        if (result == null)
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        else
            return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @DeleteMapping("/shopping/delete")
    public ResponseEntity<String> deleteReview(ProductReviewDTO productReviewDTO) {
        replyService.deleteReview(productReviewDTO);
        return new ResponseEntity<>("삭제성공", HttpStatus.OK);
    }
//-QNA----------------------------------------------------------------------------------
    @PostMapping("/shopping/addQNA")
    public ResponseEntity<ProductQnADTO> addQNA(@RequestParam("pno")Long pno, @RequestParam("question")String question, HttpServletRequest request) {
        System.out.println("pno = " + pno);
        System.out.println("question = " + question);
        HttpSession session = request.getSession();
        String email = (String)session.getAttribute("email");
        ProductQnADTO productQnADTO = new ProductQnADTO(pno,question,email);

        System.out.println(" 컨트롤러부분productQnADTO.getQuestion() = " + productQnADTO.getQuestion());
        ProductQnADTO result = replyService.addQNA(productQnADTO);
        if (result == null)
            return new ResponseEntity<>(null, HttpStatus.OK);
        else
            return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //댓글 목록보기
    @GetMapping("/shopping/getQNAList")
    public ResponseEntity<List<ProductQnADTO>> getQNAList(@RequestParam("pno") Long pno){
        System.out.println(" 컨트롤러 pno = " + pno);
        List<ProductQnADTO> list = replyService.getQNAList(pno);
        if (list.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.OK);
        else
            return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //상세보기
    @GetMapping("/shopping/getQNA")
    public ResponseEntity<ProductQnADTO> getReview(ProductQnADTO productQnADTO){
        return new ResponseEntity<>(replyService.getQNA(productQnADTO), HttpStatus.OK);
    }


    @PutMapping("/shopping/updateQNA")
    public ResponseEntity<ProductQnADTO> updateReview(@RequestParam("id")Long id, @RequestParam("content") String question) {
        ProductQnADTO productQnADTO =new ProductQnADTO(id,question);
        ProductQnADTO result = replyService.updateQNA(productQnADTO);
        if (result == null)
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        else
            return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @DeleteMapping("/shopping/deleteQNA")
    public ResponseEntity<String> deleteReview(@RequestParam("id")Long id) {
        ProductQnADTO productQnADTO =new ProductQnADTO(id);
        replyService.deleteQNA(productQnADTO);
        return new ResponseEntity<>("삭제성공", HttpStatus.OK);
    }
}
