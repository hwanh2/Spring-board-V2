package study.spring_board_V2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import study.spring_board_V2.domain.Board;
import study.spring_board_V2.domain.Comment;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.dto.CommentForm;
import study.spring_board_V2.repository.CommentRepository;

import jakarta.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BoardService boardService;

    @Mock
    private MemberService memberService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CommentService commentService;

    private Member member;
    private Board board;
    private Comment comment;

    @BeforeEach
    void setUp() {
        member = new Member();
        member.setId(1L);
        member.setName("testUser");

        board = new Board();
        board.setId(1L);
        board.setTitle("Test Board");
        board.setContent("Board content");
        board.setMember(member);

        comment = new Comment();
        comment.setId(1L);
        comment.setMember(member);
        comment.setBoard(board);
        comment.setContent("Test comment");
    }

    @Test
    void createComment_Success() {
        // given
        Long boardId = 1L;
        CommentForm form = new CommentForm();
        form.setContent("New comment content");

        when(session.getAttribute("member")).thenReturn(member);
        when(memberService.findOne(member.getId())).thenReturn(member);
        when(boardService.findById(boardId)).thenReturn(board);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // when
        Comment createdComment = commentService.createComment(session, boardId, form);

        // then
        assertNotNull(createdComment);
        assertEquals("New comment content", createdComment.getContent());
        assertEquals(member, createdComment.getMember());
        assertEquals(board, createdComment.getBoard());
    }

    @Test
    void createComment_NoMemberInSession() {
        // given
        Long boardId = 1L;
        CommentForm form = new CommentForm();
        form.setContent("New comment content");

        when(session.getAttribute("member")).thenReturn(null);

        // when & then
        assertThrows(IllegalStateException.class, () -> commentService.createComment(session, boardId, form));
    }

    @Test
    void updateComment_Success() {
        // given
        Long commentId = 1L;
        String updatedContent = "Updated comment content";

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // when
        Comment updatedComment = commentService.updateComment(commentId, member, updatedContent);

        // then
        assertNotNull(updatedComment);
        assertEquals(updatedContent, updatedComment.getContent());
    }

    @Test
    void updateComment_NotOwner() {
        // given
        Long commentId = 1L;
        Member differentMember = new Member();
        differentMember.setId(2L);  // 다른 사용자
        String updatedContent = "Updated comment content";

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // when & then
        assertThrows(IllegalStateException.class, () -> commentService.updateComment(commentId, differentMember, updatedContent));
    }

    @Test
    void findById_Success() {
        // given
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // when
        Comment foundComment = commentService.findById(commentId);

        // then
        assertNotNull(foundComment);
        assertEquals(commentId, foundComment.getId());
    }

    @Test
    void findById_NotFound() {
        // given
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> commentService.findById(commentId));
    }

    @Test
    void findByBoardId_Success() {
        // given
        Long boardId = 1L;
        when(commentRepository.findByBoardId(boardId)).thenReturn(List.of(comment));

        // when
        List<Comment> comments = commentService.findByBoardId(boardId);

        // then
        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals(board, comments.get(0).getBoard());
    }

    @Test
    void deleteComment_Success() {
        // given
        Long commentId = 1L;
        doNothing().when(commentRepository).deleteById(commentId);

        // when
        commentService.deleteComment(commentId);

        // then
        verify(commentRepository, times(1)).deleteById(commentId);
    }
}
