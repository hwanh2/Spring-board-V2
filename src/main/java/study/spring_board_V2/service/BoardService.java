package study.spring_board_V2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.spring_board_V2.domain.Board;
import study.spring_board_V2.repository.BoardRepository;

import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }
    public Board save(Board board){
        boardRepository.save(board);
        return board;
    }
    public List<Board> list(){
        return boardRepository.findAll();
    }
}