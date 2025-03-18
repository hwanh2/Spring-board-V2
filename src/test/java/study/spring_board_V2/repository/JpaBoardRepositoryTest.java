package study.spring_board_V2.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import study.spring_board_V2.domain.Board;
import study.spring_board_V2.domain.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // JPA 관련 테스트 설정
@Transactional  // 테스트 후 자동 롤백 (테스트 격리)
class JpaBoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    void save_and_findAll() {
        // given
        Member member = new Member();
        member.setName("testUser");
        member = memberRepository.save(member); // Member 저장 후 영속화

        Board board1 = new Board();
        board1.setTitle("First Post");
        board1.setContent("This is the first post content.");
        board1.setMember(member);
        boardRepository.save(board1);

        Board board2 = new Board();
        board2.setTitle("Second Post");
        board2.setContent("This is the second post content.");
        board2.setMember(member);
        boardRepository.save(board2);

        // when
        List<Board> boards = boardRepository.findAll();

        // then
        assertThat(boards).hasSize(2);
        assertThat(boards).extracting("title").containsExactlyInAnyOrder("First Post", "Second Post");
    }

    @Test
    void save_and_findAll_whenBoardIsEmpty() {
        // when
        List<Board> boards = boardRepository.findAll();

        // then
        assertThat(boards).isEmpty();
    }
}
