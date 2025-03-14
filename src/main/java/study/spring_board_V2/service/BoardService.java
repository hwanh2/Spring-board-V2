package study.spring_board_V2.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.spring_board_V2.domain.Board;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.repository.BoardRepository;

import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public Board save(Board board){
        boardRepository.save(board);
        return board;
    }


    public List<Board> list(){
        return boardRepository.findAll();
    }

    public List<Board> findByMember(Member member) {
        return boardRepository.findByMember(member); // member에 해당하는 게시글들을 반환
    }
}