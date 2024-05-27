package com.ohrray.domain;

import com.ohrray.entity.Member;
import com.ohrray.entity.Product;
import com.ohrray.entity.ProductQnA;
import com.ohrray.entity.ProductReview;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductQnADTO {

    private Long id;

    private Long pid;

    private String email;

    private String question;

    private String answer;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    public ProductQnADTO(Long id, String question) {
        this.id =id;
        this.question=question;
    }

    public ProductQnADTO(Long id) {
        this.id=id;
    }


    public ProductQnA changeEntity(ProductQnADTO productQnADTO, Product product, Member member){
        ProductQnA productQnA = new ProductQnA();
        productQnA.setMember(member);
        productQnA.setProduct(product);
        productQnA.setQuestion(productQnADTO.getQuestion());
        productQnA.setAnswer(productQnADTO.getAnswer());
        return productQnA;
    }

    public ProductQnADTO changeDTO(ProductQnA productQnA){
        this.id =productQnA.getId();
        this.pid=productQnA.getProduct().getId();
        this.email = productQnA.getMember().getEmail();
        this.question= productQnA.getQuestion();
        this.answer= productQnA.getAnswer();
        this.regDate=productQnA.getRegDate();
        this.modDate=productQnA.getModDate();
        return this;
    }
    public ProductQnADTO(Long pno ,String question,String email){
        this.pid=pno;
        this.question=question;
        this.email =email;
    }

}
