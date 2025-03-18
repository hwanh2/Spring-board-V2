package study.spring_board_V2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.spring_board_V2.domain.Board;
import study.spring_board_V2.domain.Member;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {
    List<Board> findByMember(Member member);
}
