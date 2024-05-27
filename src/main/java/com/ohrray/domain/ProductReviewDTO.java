package com.ohrray.domain;

import com.ohrray.entity.Member;
import com.ohrray.entity.Product;
import com.ohrray.entity.ProductReview;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewDTO {
    private Long id;
    private Long pno;
    private Long mid;
    private String review;
    private String answer;
    private float rating;
    private LocalDateTime regDate;
    private LocalDateTime modDate;


    public ProductReview changeEntity(ProductReviewDTO productReviewDTO, Product product, Member member){
        ProductReview productReview = new ProductReview();
        productReview.setMember(member);
        productReview.setProduct(product);
        productReview.setReview(productReviewDTO.getReview());
        productReview.setAnswer(productReviewDTO.getAnswer());
        productReview.setRating(productReviewDTO.getRating());
        return productReview;
    }

    public ProductReviewDTO changeDTO(ProductReview productReview){
        this.id =productReview.getId();
        this.pno=productReview.getProduct().getId();
        this.review= productReview.getReview();
        this.answer= productReview.getAnswer();
        this.rating=productReview.getRating();
        this.regDate=productReview.getRegDate();
        this.modDate=productReview.getModDate();
        return this;
    }

}
