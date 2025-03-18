package study.spring_board_V2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.spring_board_V2.domain.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByMemberId(Long memberId);
    List<Comment> findByBoardId(Long boardId);
}
