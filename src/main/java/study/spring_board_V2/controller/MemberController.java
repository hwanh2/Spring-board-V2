package study.spring_board_V2.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.dto.MemberForm;
import study.spring_board_V2.service.MemberService;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //회원가입
    @PostMapping("/signup")
    public Member signup(@RequestBody MemberForm form){
        Member member = new Member();
        member.setName(form.getName());
        member.setPassword(form.getPassword());
        memberService.join(member);
        return member;
    }

    //로그인
    @PostMapping("/signin")
    public Member signin(HttpSession session,@RequestBody MemberForm form){
        Member member = memberService.signin(form.getName(),form.getPassword());
        session.setAttribute("member", member);
        return member;
    }

    //회원목록
    @GetMapping("/list")
    public List<Member> list(){
        return memberService.findMembers();
    }

}
