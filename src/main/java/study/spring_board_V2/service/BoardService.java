package study.spring_board_V2.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.spring_board_V2.domain.Board;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.dto.BoardForm;
import study.spring_board_V2.repository.BoardRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberService memberService;

    @Transactional
    public Board createBoard(HttpSession session, BoardForm form) {
        // 세션에서 회원 정보 가져오기
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }

        // 회원을 DB에서 조회
        member = memberService.findOne(member.getId());
        if (member == null) {
            throw new RuntimeException("Member not found");
        }

        // 게시글 생성
        Board board = new Board(form.getTitle(),form.getContent(),member);
        return boardRepository.save(board); // 게시글 저장
    }

    @Transactional
    public Board updateBoard(Long boardId, Long memberId, BoardForm form) {
        // 게시글 찾기
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 게시글 작성자와 현재 로그인된 회원이 동일한지 체크
        if (!board.getMember().getId().equals(memberId)) {
            throw new RuntimeException("자신의 게시글만 수정할 수 있습니다.");
        }

        // 게시글 수정
        board.setTitle(form.getTitle());
        board.setContent(form.getContent());

        // 게시글 저장
        return boardRepository.save(board);
    }

    public List<Board> list(){
        return boardRepository.findAll();
    }

    public List<Board> findByMember(Member member) {
        return boardRepository.findByMember(member); // member에 해당하는 게시글들을 반환
    }

    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }

    @Transactional
    public void deleteById(Long boardId) {
        boardRepository.deleteById(boardId);
    }
}