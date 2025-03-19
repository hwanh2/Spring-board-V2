package study.spring_board_V2.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.spring_board_V2.domain.Board;
import study.spring_board_V2.domain.Comment;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.dto.CommentForm;
import study.spring_board_V2.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final MemberService memberService;

    @Transactional
    public Comment createComment(HttpSession session, Long boardId, CommentForm form) {
        // 게시글 조회
       Member member = (Member)session.getAttribute("member");
        if (member == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }
        member = memberService.findOne(member.getId());
        if (member == null) {
            throw new RuntimeException("Member not found");
        }

        Board board = boardService.findById(boardId);
        if (board == null) {
            throw new IllegalArgumentException("존재하지 않는 게시판입니다.");
        }

        // 댓글 생성
        Comment comment = new Comment(form.getContent(),member,board);
        return commentRepository.save(comment);
    }


    @Transactional
    public Comment updateComment(Long commentId, Member member, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!comment.getMember().getId().equals(member.getId())) {
            throw new IllegalStateException("본인의 댓글만 수정할 수 있습니다.");
        }

        comment.setContent(content);
        return commentRepository.save(comment);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
    }


    public List<Comment> findByBoardId(Long boardId){
        return commentRepository.findByBoardId(boardId);
    }

    public List<Comment> findByMemberId(Long memberId){
        return commentRepository.findByMemberId(memberId);
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
