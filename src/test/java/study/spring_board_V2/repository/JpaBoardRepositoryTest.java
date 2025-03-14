package study.spring_board_V2.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import study.spring_board_V2.domain.Board;
import study.spring_board_V2.domain.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // JPA 관련 테스트 설정
class JpaBoardRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    private JpaBoardRepository boardRepository;
    private JpaMemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        boardRepository = new JpaBoardRepository(em);
        memberRepository = new JpaMemberRepository(em); // memberRepository 추가
    }

    @AfterEach
    void tearDown() {
        em.createQuery("DELETE FROM Board").executeUpdate(); // Board 테이블 비우기
        em.createQuery("DELETE FROM Member").executeUpdate(); // Member 테이블 비우기
    }

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
        boardRepository.save(board1);

        Board board2 = new Board();
        board2.setTitle("Second Post");
        board2.setContent("This is the second post content.");
        board2.setMember(member); // Board 객체에 member 할당
        boardRepository.save(board2);

        em.flush(); // 영속성 컨텍스트 반영

        // when
        List<Board> boards = boardRepository.findAll();

        // then
        assertThat(boards).hasSize(2); // 게시물이 2개가 있는지 확인
        assertThat(boards).extracting("title").containsExactlyInAnyOrder("First Post", "Second Post");
    }

    @Test
    void save_and_findAll_whenBoardIsEmpty() {
        // when
        List<Board> boards = boardRepository.findAll();

        // then
        assertThat(boards).isEmpty(); // 게시물이 없을 경우 빈 리스트 확인
    }
}
