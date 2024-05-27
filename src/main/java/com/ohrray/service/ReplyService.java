package com.ohrray.service;

import com.ohrray.domain.CommunityReplyDTO;
import com.ohrray.domain.ProductQnADTO;
import com.ohrray.domain.ProductReviewDTO;

import java.util.List;

public interface ReplyService {
    //커뮤니티 댓글 ------------
    public CommunityReplyDTO addReply(CommunityReplyDTO communityReplyDTO,String email);
    public CommunityReplyDTO get(CommunityReplyDTO communityReplyDTO );
    public CommunityReplyDTO updateReply(CommunityReplyDTO communityReplyDTO);
    public void deleteReply(CommunityReplyDTO communityReplyDTO);
    public List<CommunityReplyDTO> getList(Long bid);

    // 리뷰 -------------------------
    public List<ProductReviewDTO> getReviewList(Long pno);
    public ProductReviewDTO get(ProductReviewDTO productReviewDTO);
    public ProductReviewDTO addReview(ProductReviewDTO productReviewDTO ,Long orderDetailId);
    public ProductReviewDTO updateReview(ProductReviewDTO productReviewDTO);
    public void deleteReview(ProductReviewDTO productReviewDTO);


    // QNA ------------------------------
    public List<ProductQnADTO> getQNAList(Long pno);
    public ProductQnADTO getQNA(ProductQnADTO productQnADTO);
    public ProductQnADTO addQNA(ProductQnADTO productQnADTO);
    public ProductQnADTO updateQNA(ProductQnADTO productQnADTO);
    public void deleteQNA(ProductQnADTO productQnADTO);
}
