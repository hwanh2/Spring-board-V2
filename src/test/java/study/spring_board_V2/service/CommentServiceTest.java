package study.spring_board_V2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.spring_board_V2.domain.Board;
import study.spring_board_V2.domain.Comment;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.repository.BoardRepository;
import study.spring_board_V2.repository.CommentRepository;
import study.spring_board_V2.repository.MemberRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    private Member member;
    private Board board;

    @BeforeEach
    void setUp() {
        // 테스트용 Member, Board 엔티티 생성
        member = new Member();
        member.setName("testUser");
        memberRepository.save(member);

        board = new Board();
        board.setTitle("testBoard");
        board.setContent("Test content");
        board.setMember(member);
        boardRepository.save(board);

        // H2 DB에 저장될 Comment 객체 생성
        Comment comment = new Comment();
        comment.setContent("테스트 댓글");
        comment.setMember(member);
        comment.setBoard(board);

        commentService.save(comment);
    }

    @Test
    void 댓글저장_테스트() {
        // given
        Comment newComment = new Comment();
        newComment.setContent("새로운 댓글");
        newComment.setMember(member);
        newComment.setBoard(board);

        // when
        Comment savedComment = commentService.save(newComment);

        // then
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo("새로운 댓글");
    }

    @Test
    void 댓글ID로_찾기_테스트() {
        // given
        List<Comment> comments = commentService.findByBoardId(board.getId());
        Comment comment = comments.get(0);

        // when
        Comment foundComment = commentService.findById(comment.getId());

        // then
        assertThat(foundComment).isNotNull();
        assertThat(foundComment.getContent()).isEqualTo(comment.getContent());
    }

    @Test
    void 게시판ID로_댓글찾기_테스트() {
        // when
        List<Comment> comments = commentService.findByBoardId(board.getId());

        // then
        assertThat(comments).isNotEmpty();
        assertThat(comments.get(0).getBoard().getId()).isEqualTo(board.getId());
    }

    @Test
    void 댓글삭제_테스트() {
        // given
        List<Comment> comments = commentService.findByBoardId(board.getId());
        Comment comment = comments.get(0);
        Long commentId = comment.getId();

        // when
        commentService.deleteComment(commentId);
        Comment deletedComment = commentService.findById(commentId);

        // then
        assertThat(deletedComment).isNull();
    }
}
