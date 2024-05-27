package com.ohrray.service;

import com.ohrray.domain.CommunityBoardDTO;
import com.ohrray.domain.MapDTO;
import com.ohrray.domain.PageRequestDTO;
import com.ohrray.entity.Community;
import jakarta.servlet.ServletException;

import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface CommunityService {
    //산 id로 커뮤니티 및 게시판 정보 불러오기.
    public Community getCommunityList(Long id);
    //게시판 등록
    public void registerBoard(CommunityBoardDTO boardDTO,String email) throws Exception;
    //게시판 글 전체조회
    public Page<CommunityBoardDTO> getCommunityBoardList(Long id, PageRequestDTO pageRequestDTO);
    //게시판 글 상세조회
    public CommunityBoardDTO getOneBoard(Long bid);
    //게시판글 수정
    public void updateBoard(CommunityBoardDTO communityBoardDTO) throws Exception;
    //게시글 삭제
    public Long deleteBoard(Long bid);

    //검색어로 산 정보 가져오기
    public Page<MapDTO> getMountainInformWithKeyword(String keyword,Integer pageNum);
    //처음 산정보 API통해 입력하기
    public void insertMountainInform() throws IOException, ParseException;
    //검색어 가지고 네이버 API이용해서 가져오기.
    public String searchNaver(String keyword) throws ServletException, IOException;
}
