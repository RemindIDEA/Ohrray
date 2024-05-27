package com.ohrray.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityImg {
    @Id @GeneratedValue
    @Column(name = "COMMUNITY_IMG_ID")
    private Long id;


    private String mainImg1;
    private String mainImg2;
    private String mainImg3;
    private String mainImg4;
}
