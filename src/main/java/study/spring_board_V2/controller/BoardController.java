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


}
