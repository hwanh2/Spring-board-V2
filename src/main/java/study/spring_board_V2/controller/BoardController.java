package study.spring_board_V2.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import study.spring_board_V2.domain.Board;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.dto.BoardForm;
import study.spring_board_V2.service.BoardService;
import study.spring_board_V2.service.MemberService;

import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {
    private final MemberService memberService;
    private final BoardService boardService;

    @Autowired
    public BoardController(MemberService memberService, BoardService boardService) {
        this.memberService = memberService;
        this.boardService = boardService;
    }

    @PostMapping("/create")
    public Board createBoard(HttpSession session, @RequestBody BoardForm form){
        Member member = (Member) session.getAttribute("member");

        if (member != null) {
            member = memberService.findOne(member.getId());
            if (member == null) {
                throw new RuntimeException("Member not found");
            }
        }

        Board board = new Board();
        board.setTitle(form.getTitle());
        board.setContent(form.getContent());
        board.setMember(member);

        return boardService.save(board);  // BoardService를 통해 저장
    }

    @GetMapping("/list")
    public List<Board> list(){
        return boardService.list();
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{boardId}")
    public String deleteBoard(HttpSession session, @PathVariable Long boardId) {
        Member member = (Member) session.getAttribute("member");
        System.out.println(member);
        if (member != null) {
            member = memberService.findOne(member.getId());
            if (member == null) {
                throw new RuntimeException("Member not found");
            }
        }

        // 게시글 조회
        Board board = boardService.findById(boardId);
        System.out.println(board.getMember());

        System.out.println("세션에서 가져온 사용자 ID: " + (member != null ? member.getId() : "null"));

        if (member != null) {
            member = memberService.findOne(member.getId());
            if (member == null) {
                throw new RuntimeException("DB에서 회원을 찾을 수 없습니다.");
            }
            System.out.println("DB에서 찾은 회원 ID: " + member.getId());
        }

        if (board == null) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }

        // 로그인한 회원이 게시글 작성자인지 확인
        if (!board.getMember().getId().equals(member.getId())) {
            throw new RuntimeException("자신의 게시글만 삭제할 수 있습니다.");
        }

        // 게시글 삭제
        boardService.deleteById(boardId);
        return "게시글이 삭제되었습니다.";
    }


}
