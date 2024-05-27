package com.ohrray.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Faq {
    //FAQ PK -> Auto Increment
    @Id
    @GeneratedValue
    @Column(name = "FAQ_ID")
    private Long id;

    //질문 내용
    private String title;
    //질문 답변
    private String content;

}
