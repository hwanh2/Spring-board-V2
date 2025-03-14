package study.spring_board_V2.repository;

import study.spring_board_V2.domain.Comment;

import java.util.List;

public interface CommentRepository {
    Comment Save(Comment comment);
    Comment findById(Long id);
    List<Comment> findByMemberId(Long memberId);
    List<Comment> findByBoardId(Long boardId);
    void deleteById(Long id);
}
