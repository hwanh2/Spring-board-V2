package study.spring_board_V2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.spring_board_V2.domain.Board;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.repository.BoardRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

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

    public Board findById(Long id){
        return boardRepository.findById(id);
    }

    @Transactional
    public void deleteById(Long boardId) {
        boardRepository.deleteById(boardId);
    }
}