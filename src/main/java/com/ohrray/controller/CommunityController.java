package com.ohrray.controller;

import com.ohrray.domain.CommunityBoardDTO;
import com.ohrray.domain.MapDTO;
import com.ohrray.domain.MemberDTO;
import com.ohrray.domain.PageRequestDTO;
import com.ohrray.service.CommunityService;
import com.ohrray.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {
    //자동주입.
    private final CommunityService communityService;
    private final MemberService memberService;


    //Json 공공데이터 저장 함수
    //산 정보 집어넣기.(한번만 집어넣고 그다음부터는 거절)
    @GetMapping("/index")
    public String insertMapInform() throws IOException, ParseException {
        communityService.insertMountainInform();
        return "redirect:/community/main";
    }
    //커뮤니티 메인화면
    @GetMapping("/main")
    public void goMain(){}

    //선택된 산 커뮤니티 게시판
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/board")
    public void goBoard(@RequestParam("id")Long cid, PageRequestDTO pageRequestDTO , Model model) {
        System.out.println("pageRequestDTO = " + pageRequestDTO.getSize());
        System.out.println("pageRequestDTO = " + pageRequestDTO.getPage());
        System.out.println("cid = " + cid);

        //산이름 가져오기.
        model.addAttribute("community",communityService.getCommunityList(cid));
        //가져온 산 커뮤니티의 R 보여주기
        model.addAttribute("resultList",communityService.getCommunityBoardList(cid,pageRequestDTO));
        System.out.println("communityService.getCommunityBoardList(cid,pageRequestDTO).toString() = " + communityService.getCommunityBoardList(cid,pageRequestDTO).toString());
    }

    //게시판 새글 작성
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/register")
    public void goRegister(@RequestParam("id") Long id, Model model ){
        model.addAttribute("id" , id);

    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/register")
    public String insertBoard( CommunityBoardDTO boardDTO,HttpServletRequest request) throws  Exception{
        HttpSession session = request.getSession();
        String email = (String)session.getAttribute("email");
        communityService.registerBoard(boardDTO,email);
        return "redirect:/community/board?id="+boardDTO.getCid();
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/read")
    public void getOneBoard(@RequestParam("bid")Long bid, @RequestParam("id")Long cid, Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        String email = (String)session.getAttribute("email");
        MemberDTO memberByEmail = memberService.getMemberByEmail(email);
        System.out.println("memberByEmail.getId() = " + memberByEmail.getId());
        CommunityBoardDTO oneBoard = communityService.getOneBoard(bid);
        model.addAttribute("result",communityService.getOneBoard(bid));
        model.addAttribute("id",cid);
        model.addAttribute("member",memberByEmail);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/update")
    public void getOneBoard(@RequestParam("bid")Long bid, Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        String email = (String)session.getAttribute("email");
        MemberDTO memberByEmail = memberService.getMemberByEmail(email);
        CommunityBoardDTO oneBoard = communityService.getOneBoard(bid);
        model.addAttribute("result",communityService.getOneBoard(bid));
        model.addAttribute("member",memberByEmail);
    }


    //게시판 글 업데이트
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/update")
    public String updateBoard(CommunityBoardDTO communityBoardDTO) throws Exception{
        communityService.updateBoard(communityBoardDTO);
        return "redirect:/community/board?id="+communityBoardDTO.getCid();
    }
    //게시판 글 삭제
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/delete")
    public String deleteBoard(Long bid){
        System.out.println("bid = " + bid);
        Long result = communityService.deleteBoard(bid);
        return  "redirect:/community/board?id="+result;
    }

    //네이버 블로그 검색 API
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/search")
    public void searchNaver(@RequestParam("keyword")String keyword, HttpServletResponse response) throws ServletException, IOException {

        String responseBody = communityService.searchNaver(keyword);

        //결과값 보내주기
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().write(responseBody);  // 서블릿에서 즉시 출력
    }
    //산 정보 가져오기
    @GetMapping("/searchKeyword")
    public ResponseEntity<Page<MapDTO>> searchMapInform(@RequestParam("keyword")String keyword, @RequestParam("pageNum") int pageNum) throws ServletException, IOException {
        System.out.println("pageNum = " + pageNum);
        Page<MapDTO> lists = communityService.getMountainInformWithKeyword(keyword, pageNum);
        if(lists.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        else
            return new ResponseEntity<>(lists, HttpStatus.OK);
    }


    //날씨 정보 가져오기 API



}
