package study.spring_board_V2.repository;

import study.spring_board_V2.domain.Board;

import java.util.List;

public interface BoardRepository {
    Board save(Board board);
    List<Board> findAll();
}
