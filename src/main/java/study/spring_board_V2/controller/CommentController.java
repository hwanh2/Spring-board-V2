package study.spring_board_V2.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import study.spring_board_V2.domain.Board;
import study.spring_board_V2.domain.Comment;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.dto.CommentForm;
import study.spring_board_V2.service.BoardService;
import study.spring_board_V2.service.CommentService;
import study.spring_board_V2.service.MemberService;

@RestController
@RequestMapping("/board/{boardId}")
public class CommentController {
    private final MemberService memberService;
    private final BoardService boardService;
    private final CommentService commentService;

    @Autowired
    public CommentController(MemberService memberService, BoardService boardService, CommentService commentService) {
        this.memberService = memberService;
        this.boardService = boardService;
        this.commentService = commentService;
    }

    @PostMapping("/comments")
    public Comment createComment(HttpSession session, @PathVariable Long boardId, @RequestBody CommentForm form){
        Member member = (Member)session.getAttribute("member");
        if (member == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }
        member = memberService.merge(member);

        Board board = boardService.findById(boardId);
        if (board == null) {
            throw new IllegalArgumentException("존재하지 않는 게시판입니다."); // 게시판이 없는 경우 예외 처리
        }

        Comment comment = new Comment();
        comment.setMember(member);
        comment.setBoard(board);
        comment.setContent(form.getContent());
        commentService.save(comment);
        return comment;

    }

}
