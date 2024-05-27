package com.ohrray.controller;

import com.ohrray.domain.FaqDTO;
import com.ohrray.domain.MemberDTO;
import com.ohrray.domain.SecurityMemberDTO;
import com.ohrray.service.FaqService;
import com.ohrray.service.MemberService;
import com.ohrray.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/member")
@NoArgsConstructor
public class MemberController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private FaqService faqService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /*회원 관련 내용만 ------------------------------------------------*/
    //로그인 페이지로 이동시켜준다.
    //getMapping 이 종료되면 mapping name 과 같은 html 찾아서 이동
    @GetMapping("/login")
    public void goLoginPage(){}

    //서비스에 넘겨줄것
    //시큐리티때문에 아마 안쓸것.
    @PostMapping("/login")
    public void login(){
        System.out.println("여기에 들어와지는 건가????");
        //사용 안함.
    }

    //회원가입페이지
    @PostMapping("/joinMember")
    public String joinMember(MemberDTO memberDTO,RedirectAttributes rttr){
        memberService.join(memberDTO);
        rttr.addFlashAttribute("msg","회원가입이 완료되었습니다.");
        return "redirect:/member/login";
    }
    /*회원 정보수정-----------------------------------------------*/
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping({"/update","/firstUpdate","/deleteMember1"})
    public void updateMember(HttpServletRequest request,Model model){
        HttpSession session = request.getSession();
        System.out.println("securityMemberDTO.getEmail() = " + session.getAttribute("email"));
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail((String)session.getAttribute("email"));
        model.addAttribute("member",memberService.getMember(memberDTO));
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/update")
    public String updateMember(MemberDTO memberDTO){
        memberService.updateMember(memberDTO);
        return "redirect:/shopping/main";
    }
    /*회원 정보수정-----------------------------------------------*/

    /*소셜 회원 처음 정보수정-----------------------------------------------*/
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/firstUpdate")
    public String firstUpdateMember(MemberDTO memberDTO){
        memberService.updateFirst(memberDTO);
        return "redirect:/shopping/main";
    }
    /*소셜 회원 처음 정보수정-----------------------------------------------*/

    /*회원 가입시 아이디 중복 확인-----------------------------------------------*/
    @GetMapping("/validation")
    public ResponseEntity<String> idValidation(String email) {
        System.out.println("email = " + email);
        System.out.println("컨트롤러로 넘어오나");
        //회원이 중복이 아닐경우
        System.out.println("result is ===============" + memberService.validateMember(email));
        if (memberService.validateMember(email)){
            System.out.println("success");
            return new ResponseEntity<>(email, HttpStatus.OK);
        }else{
            //회원이 중복일 경우
            System.out.println("failure");
            return new ResponseEntity<>("중복된 ID입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /*회원 가입시 아이디 중복 확인-----------------------------------------------*/
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/deleteMember")
    public void deleteMemberForm(){
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/deleteMember")
    public String deleteMember(MemberDTO memberDTO,HttpServletRequest request,RedirectAttributes rttr) {
        HttpSession session = request.getSession();
        String email = (String)session.getAttribute("email");
        System.out.println("여기는 멤버삭제 존");
        System.out.println("email = " + memberDTO.getEmail());
        System.out.println("memberDTO = " + memberDTO.getPassword());
        if(email.equals(memberDTO.getEmail())){
            System.out.println("아이디랑 세션 메일이랑 같음.");
            boolean result = memberService.deleteMember(memberDTO);
            if(result) {
                System.out.println("success");
                session.invalidate();
                return "redirect:/member/login";
            }else{
                rttr.addFlashAttribute("msg","입력된 비밀번호가 잘못 되었습니다.");
                return "redirect:/member/deleteMember";
            }
        }else{
            rttr.addFlashAttribute("msg","입력된 이메일이 잘못 되었습니다.");
            return "redirect:/member/deleteMember";
        }

    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myPage")
    public void myPage(HttpServletRequest request , Model model){
        //세션으로 id가져오기
        HttpSession session = request.getSession();
        String email = (String)session.getAttribute("email");
        System.out.println("email = " + email);
        //id로 구매내역 불러오기
        model.addAttribute("detail",orderService.getOrderDetail(email));

    }

    /*회원 관련 내용만 끝------------------------------------------------*/


    /*FAQ 관련 내용만-----------------------------------------------------------*/
    //목록
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/faq")
    public String listFaqs(Model model) {
        List<FaqDTO> faqs = faqService.getFaqs();
        model.addAttribute("faqs", faqs);
        return "member/faq";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/registerFAQ")
    public void showCreateFaqForm(Model model) {
    }

    //작성
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/registerFAQ")
    public String createFaq(@ModelAttribute FaqDTO faqDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.faqDTO", bindingResult);
            redirectAttributes.addFlashAttribute("faqDTO", faqDTO);
            return "redirect:/member/faqContent";
        }
        if (faqDTO.getId() == null) {
            faqService.saveFaq(faqDTO);
            redirectAttributes.addFlashAttribute("successMessage", "FAQ created successfully!");
        }
        return "redirect:/member/faq";
    }

    //수정 페이지 보여주기 id로 찾아옴
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/updateFAQ")
    public void showEditFaqForm(@RequestParam("id") Long id, Model model) {
        System.out.println("컨트롤ㄹ러 부붑ㄴ id = " + id);
        //이부분 수정 필요.
        FaqDTO faqById = faqService.getFaqById(id);
        model.addAttribute("faq",faqById);
    }

    //수정하기
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateFAQ")
    public String updateFaq( @ModelAttribute FaqDTO faqDTO, RedirectAttributes redirectAttributes) {
        System.out.println("faqDTO.getId() = " + faqDTO.getId());
        System.out.println("faqDTO.getTitle() = " + faqDTO.getTitle());
        System.out.println("faqDTO.getContent() = " + faqDTO.getContent());
        faqService.updateFaq(faqDTO);
        redirectAttributes.addFlashAttribute("successMessage", "FAQ updated successfully!");
        return "redirect:/member/faq";
    }

    //삭제하기
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteFAQ")
    public String deleteFaq(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        faqService.deleteFaq(id);
        redirectAttributes.addFlashAttribute("successMessage", "FAQ deleted successfully!");

        System.out.println("delete = " + id);

        return "redirect:/member/faq";
    }
}
