package study.spring_board_V2.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.spring_board_V2.domain.Board;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.repository.JpaBoardRepository;
import study.spring_board_V2.repository.JpaMemberRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private JpaBoardRepository boardRepository;

    @Autowired
    private JpaMemberRepository memberRepository;

    @Test
    void save_and_findAll() {
        // given
        Member member = new Member();
        member.setName("testUser");
        memberRepository.save(member); // Member 객체 저장

        Board board1 = new Board();
        board1.setTitle("First Post");
        board1.setContent("This is the first post content.");
        board1.setMember(member); // Board 객체에 member 할당

        Board board2 = new Board();
        board2.setTitle("Second Post");
        board2.setContent("This is the second post content.");
        board2.setMember(member); // Board 객체에 member 할당

        // when
        boardService.save(board1);
        boardService.save(board2);

        // then
        List<Board> boards = boardService.list();
        assertThat(boards).hasSize(2); // 게시물이 2개 있는지 확인
        assertThat(boards).extracting("title").containsExactlyInAnyOrder("First Post", "Second Post");
    }

    @Test
    void save_and_findAll_whenBoardIsEmpty() {
        // when
        List<Board> boards = boardService.list();

        // then
        assertThat(boards).isEmpty(); // 게시물이 없을 경우 빈 리스트 확인
    }
}
