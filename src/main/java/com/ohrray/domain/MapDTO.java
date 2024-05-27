package com.ohrray.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MapDTO {
    private Long id;
    //산 이름
    private String name;
    //산 주소
    private String address;
    //높이
    private float height;
    //내용
    private String content;
    //위도
    private float lat;
    //경도
    private float lng;
    //x좌표
    private float x;
    //y좌표
    private float y;

    public MapDTO(String name, String address, float height, String content, float lat, float lng, float x, float y){
        this.name = name;
        this.address =address;
        this.height = height;
        this.content = content;
        this.lat = lat;
        this.lng = lng;
        this.x = x;
        this.y = y;
    }
    public MapDTO(Long id, String name, String address, float height, String content, float lat, float lng, float x, float y){
        this.id = id;
        this.name = name;
        this.address =address;
        this.height = height;
        this.content = content;
        this.lat = lat;
        this.lng = lng;
        this.x = x;
        this.y = y;
    }
}
