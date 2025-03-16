package study.spring_board_V2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Board", description = "게시글 관련 API")
@RequestMapping("/boards")
public class BoardController {
    private final MemberService memberService;
    private final BoardService boardService;

    @Autowired
    public BoardController(MemberService memberService, BoardService boardService) {
        this.memberService = memberService;
        this.boardService = boardService;
    }

    @Operation(summary = "게시글 생성", description = "로그인한 사용자가 게시글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글이 성공적으로 생성됨"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 오류"),
            @ApiResponse(responseCode = "401", description = "로그인되지 않음")
    })
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

        return boardService.save(board);
    }

    @Operation(summary = "게시글 목록 조회", description = "모든 게시글을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공")
    })
    @GetMapping("/list")
    public List<Board> list(){
        return boardService.list();
    }

    @Operation(summary = "게시글 수정", description = "로그인한 사용자가 본인의 게시글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "401", description = "로그인되지 않음"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @PutMapping("/update/{boardId}")
    public Board updateBoard(HttpSession session, @PathVariable Long boardId, @RequestBody BoardForm form){
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }
        member = memberService.merge(member);

        Board board = boardService.findById(boardId);
        if (board == null) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }

        if (!board.getMember().getId().equals(member.getId())) {
            throw new RuntimeException("자신의 게시글만 수정할 수 있습니다.");
        }

        board.setTitle(form.getTitle());
        board.setContent(form.getContent());

        return boardService.save(board);
    }

    @Operation(summary = "게시글 삭제", description = "로그인한 사용자가 본인의 게시글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "로그인되지 않음"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @DeleteMapping("/delete/{boardId}")
    public String deleteBoard(HttpSession session, @PathVariable Long boardId) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }
        member = memberService.merge(member);

        Board board = boardService.findById(boardId);
        if (board == null) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }

        if (!board.getMember().getId().equals(member.getId())) {
            throw new RuntimeException("자신의 게시글만 삭제할 수 있습니다.");
        }

        boardService.deleteById(boardId);
        return "게시글이 삭제되었습니다.";
    }
}
