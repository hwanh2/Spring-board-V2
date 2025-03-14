package study.spring_board_V2.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.spring_board_V2.domain.Board;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.dto.MemberForm;
import study.spring_board_V2.service.BoardService;
import study.spring_board_V2.service.MemberService;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final BoardService boardService;

    public MemberController(MemberService memberService, BoardService boardService) {
        this.memberService = memberService;
        this.boardService = boardService;
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

    //회원정보
    @GetMapping("/member")
    public Member member(HttpSession session){
        Member member = (Member) session.getAttribute("member");

        if (member == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }

        return member;
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // 세션 삭제
        return ResponseEntity.ok("로그아웃되었습니다.");
    }

    //회원목록
    @GetMapping("/list")
    public List<Member> list(){
        return memberService.findMembers();
    }

    @GetMapping("/mypage/boards")
    public List<Board> myPageBoards(HttpSession session) {
        Member member = (Member) session.getAttribute("member");

        if (member == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }

        return boardService.findByMember(member); // 멤버 객체의 게시글들을 반환
    }

}
