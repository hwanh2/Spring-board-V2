package study.spring_board_V2.repository;

import study.spring_board_V2.domain.Board;
import study.spring_board_V2.domain.Member;

import java.util.List;

public interface BoardRepository {
    Board save(Board board);
    List<Board> findAll();
    List<Board> findByMember(Member member);
    Board findById(Long id);
    void deleteById(Long id);
}
