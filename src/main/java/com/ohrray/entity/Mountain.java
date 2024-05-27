package com.ohrray.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mountain {
    @Id @GeneratedValue
    @Column(name = "MOUNTAIN_ID")
    private Long id;
    //산이름
    private String name;
    //산소재지
    private String address;
    //산 높이
    private float height;
    //경도 위도
    private float lat;
    private float lng;
    //경도위도 -> x, y 좌표 변환
    private float x;
    private float y;
    //산 설명
    @Column(columnDefinition = "TEXT")
    private String content;
}
