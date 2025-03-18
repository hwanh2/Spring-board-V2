package study.spring_board_V2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.spring_board_V2.domain.Board;
import study.spring_board_V2.domain.Comment;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.dto.MemberForm;
import study.spring_board_V2.service.BoardService;
import study.spring_board_V2.service.CommentService;
import study.spring_board_V2.service.MemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관련 API")
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final BoardService boardService;
    private final CommentService commentService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공")
    })
    @PostMapping("/signup")
    public Member signup(@RequestBody MemberForm form){
        Member member = new Member();
        member.setName(form.getName());
        member.setPassword(form.getPassword());
        memberService.join(member);
        return member;
    }

    @Operation(summary = "로그인", description = "사용자가 로그인하여 세션을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "잘못된 사용자 정보")
    })
    @PostMapping("/signin")
    public Member signin(HttpSession session,@RequestBody MemberForm form){
        Member member = memberService.signin(form.getName(),form.getPassword());
        session.setAttribute("member", member);
        return member;
    }

    @Operation(summary = "회원 정보 조회", description = "현재 로그인된 사용자의 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 반환"),
            @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
    })
    @GetMapping("/member")
    public Member member(HttpSession session){
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }
        return member;
    }

    @Operation(summary = "로그아웃", description = "현재 로그인된 사용자를 로그아웃합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃되었습니다.");
    }

    @Operation(summary = "회원 목록 조회", description = "모든 회원의 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 목록 반환")
    })
    @GetMapping("/list")
    public List<Member> list(){
        return memberService.findMembers();
    }

    @Operation(summary = "사용자가 작성한 게시글 조회", description = "로그인한 사용자가 작성한 게시글 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 목록 반환"),
            @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
    })
    @GetMapping("/mypage/boards")
    public List<Board> myPageBoards(HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }
        return boardService.findByMember(member);
    }

    @Operation(summary = "사용자가 작성한 댓글 조회", description = "로그인한 사용자가 작성한 댓글 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 목록 반환"),
            @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
    })
    @GetMapping("/mypage/comments")
    public List<Comment> myPageComments(HttpSession session){
        Member member = (Member)session.getAttribute("member");
        if (member == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }
        return commentService.findByMemberId(member.getId());
    }
}
