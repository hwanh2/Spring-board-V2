package study.spring_board_V2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.spring_board_V2.domain.Board;
import study.spring_board_V2.domain.Comment;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardService boardService;

    @Transactional
    public Comment createComment(Member member, Long boardId, String content) {
        // 게시글 조회
        Board board = boardService.findById(boardId);
        if (board == null) {
            throw new IllegalArgumentException("존재하지 않는 게시판입니다.");
        }

        // 댓글 생성
        Comment comment = new Comment();
        comment.setMember(member);
        comment.setBoard(board);
        comment.setContent(content);

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
