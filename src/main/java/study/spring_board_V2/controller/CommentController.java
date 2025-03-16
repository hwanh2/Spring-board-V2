package study.spring_board_V2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.util.List;

@RestController
@Tag(name = "Comment", description = "댓글 관련 API")
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

    @Operation(summary = "댓글 생성", description = "로그인한 사용자가 특정 게시글에 댓글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글이 성공적으로 작성됨"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 오류"),
            @ApiResponse(responseCode = "401", description = "로그인되지 않음"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @PostMapping("/comments")
    public Comment createComment(HttpSession session, @PathVariable Long boardId, @RequestBody CommentForm form){
        Member member = (Member)session.getAttribute("member");
        if (member == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }
        member = memberService.merge(member);

        Board board = boardService.findById(boardId);
        if (board == null) {
            throw new IllegalArgumentException("존재하지 않는 게시판입니다.");
        }

        Comment comment = new Comment();
        comment.setMember(member);
        comment.setBoard(board);
        comment.setContent(form.getContent());
        commentService.save(comment);
        return comment;
    }

    @Operation(summary = "게시글의 댓글 목록 조회", description = "특정 게시글에 달린 모든 댓글을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @GetMapping("/list")
    public List<Comment> list(@PathVariable Long boardId){
        return commentService.findByBoardId(boardId);
    }

    @Operation(summary = "댓글 삭제", description = "로그인한 사용자가 본인의 댓글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "로그인되지 않음"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음")
    })
    @DeleteMapping("/comments/{commentId}")
    public String deleteComment(HttpSession session, @PathVariable Long boardId, @PathVariable Long commentId){
        Member member = (Member)session.getAttribute("member");
        if (member == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }
        member = memberService.merge(member);

        Comment comment = commentService.findById(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("댓글을 찾을 수 없습니다.");
        }

        if (!comment.getMember().getId().equals(member.getId())) {
            throw new IllegalStateException("본인의 댓글만 삭제할 수 있습니다.");
        }

        commentService.deleteComment(commentId);
        return "댓글이 삭제되었습니다.";
    }

    @Operation(summary = "댓글 수정", description = "로그인한 사용자가 본인의 댓글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "401", description = "로그인되지 않음"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음")
    })
    @PutMapping("/comments/{commentId}")
    public Comment updateComment(HttpSession session, @PathVariable Long boardId, @PathVariable Long commentId, @RequestBody CommentForm form){
        Member member = (Member)session.getAttribute("member");
        if (member == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }
        member = memberService.merge(member);

        Comment comment = commentService.findById(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("댓글을 찾을 수 없습니다.");
        }

        if (!comment.getMember().getId().equals(member.getId())) {
            throw new IllegalStateException("본인의 댓글만 수정할 수 있습니다.");
        }

        comment.setContent(form.getContent());
        commentService.save(comment);
        return comment;
    }
}
