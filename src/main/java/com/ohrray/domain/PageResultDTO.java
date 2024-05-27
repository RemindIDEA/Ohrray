package com.ohrray.domain;


import com.ohrray.entity.Product;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
@RequiredArgsConstructor
public class PageResultDTO<DTO,EN>{


    //DTO 리스트
    private List<ProductFormDTO> dtoList;
    //총페이지 번호
    private int totalPage;
    //현재 페이지 번호
    private int page;
    //목록 사이즈
    private int size;
    //시작 페이지 번호, 끝 페이지 번호
    private int start, end;
    //이전, 다음
    private boolean prev, next;
    //페이지 번호  목록
    private List<Integer> pageList;

    public PageResultDTO(Page<EN> result , List<ProductFormDTO> productFormDTOList ){

        dtoList =productFormDTOList;
        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }


    public void makePageList(Pageable pageable){

        this.page = pageable.getPageNumber() + 1; // 0부터 시작하므로 1을 추가
        this.size = pageable.getPageSize();

        //temp end page
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        start = tempEnd - 9;
        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd: totalPage;
        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList()); // 1~10페이지 번호

    }



}
