package study.spring_board_V2.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import study.spring_board_V2.domain.Comment;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.domain.Board;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class JpaCommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    private Member member;
    private Board board;

    @BeforeEach
    void setUp() {
        // 테스트용 Member 저장
        member = new Member();
        member.setName("testUser");
        member.setPassword("password123");
        member = memberRepository.save(member); // 영속화

        // 테스트용 Board 저장
        board = new Board();
        board.setTitle("Test Board");
        board.setContent("This is a test board.");
        board.setMember(member);
        board = boardRepository.save(board);
    }

    @Test
    void testSaveComment() {
        // Given
        Comment comment = new Comment();
        comment.setContent("This is a test comment.");
        comment.setMember(member);
        comment.setBoard(board);

        // When
        Comment savedComment = commentRepository.save(comment);

        // Then
        assertThat(savedComment.getId()).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo("This is a test comment.");
    }

    @Test
    void testFindById() {
        // Given
        Comment comment = new Comment();
        comment.setContent("Find me!");
        comment.setMember(member);
        comment.setBoard(board);
        Comment savedComment = commentRepository.save(comment);

        // When
        Comment foundComment = commentRepository.findById(savedComment.getId()).orElse(null);

        // Then
        assertThat(foundComment).isNotNull();
        assertThat(foundComment.getContent()).isEqualTo("Find me!");
    }

    @Test
    void testFindByMemberId() {
        // Given
        Comment comment1 = new Comment();
        comment1.setContent("Member Comment 1");
        comment1.setMember(member);
        comment1.setBoard(board);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setContent("Member Comment 2");
        comment2.setMember(member);
        comment2.setBoard(board);
        commentRepository.save(comment2);

        // When
        List<Comment> comments = commentRepository.findByMemberId(member.getId());

        // Then
        assertThat(comments).hasSize(2);
        assertThat(comments).extracting("content").containsExactlyInAnyOrder("Member Comment 1", "Member Comment 2");
    }

    @Test
    void testFindByBoardId() {
        // Given
        Comment comment1 = new Comment();
        comment1.setContent("Board Comment 1");
        comment1.setMember(member);
        comment1.setBoard(board);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setContent("Board Comment 2");
        comment2.setMember(member);
        comment2.setBoard(board);
        commentRepository.save(comment2);

        // When
        List<Comment> comments = commentRepository.findByBoardId(board.getId());

        // Then
        assertThat(comments).hasSize(2);
        assertThat(comments).extracting("content").containsExactlyInAnyOrder("Board Comment 1", "Board Comment 2");
    }

    @Test
    void testDeleteById() {
        // Given
        Comment comment = new Comment();
        comment.setContent("Delete me!");
        comment.setMember(member);
        comment.setBoard(board);
        Comment savedComment = commentRepository.save(comment);

        // When
        commentRepository.deleteById(savedComment.getId());
        boolean exists = commentRepository.findById(savedComment.getId()).isEmpty();

        // Then
        assertThat(exists).isTrue();
    }
}
