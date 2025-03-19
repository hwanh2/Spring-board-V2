package study.spring_board_V2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.spring_board_V2.domain.Board;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.dto.BoardForm;
import study.spring_board_V2.repository.BoardRepository;
import study.spring_board_V2.repository.MemberRepository;

import jakarta.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private BoardService boardService;

    private Member member;
    private Board board;

    @BeforeEach
    void setUp() {
        member = new Member();
        member.setId(1L);
        member.setName("testUser");

        board = new Board();
        board.setId(1L);
        board.setTitle("Test Title");
        board.setContent("Test Content");
        board.setMember(member);
    }

    @Test
    void createBoard_Success() {
        // given
        BoardForm form = new BoardForm();
        form.setTitle("New Title");
        form.setContent("New Content");

        when(session.getAttribute("member")).thenReturn(member);
        when(memberService.findOne(member.getId())).thenReturn(member);
        when(boardRepository.save(any(Board.class))).thenReturn(board);

        // when
        Board createdBoard = boardService.createBoard(session, form);

        // then
        assertNotNull(createdBoard);
        assertEquals("New Title", createdBoard.getTitle());
        assertEquals("New Content", createdBoard.getContent());
        assertEquals(member, createdBoard.getMember());
    }

    @Test
    void createBoard_NoMemberInSession() {
        // given
        BoardForm form = new BoardForm();
        form.setTitle("New Title");
        form.setContent("New Content");

        when(session.getAttribute("member")).thenReturn(null);

        // when & then
        assertThrows(IllegalStateException.class, () -> boardService.createBoard(session, form));
    }

    @Test
    void updateBoard_Success() {
        // given
        Long boardId = 1L;
        Long memberId = 1L;
        BoardForm form = new BoardForm();
        form.setTitle("Updated Title");
        form.setContent("Updated Content");

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(boardRepository.save(any(Board.class))).thenReturn(board);

        // when
        Board updatedBoard = boardService.updateBoard(boardId, memberId, form);

        // then
        assertNotNull(updatedBoard);
        assertEquals("Updated Title", updatedBoard.getTitle());
        assertEquals("Updated Content", updatedBoard.getContent());
    }

    @Test
    void updateBoard_NotAuthor() {
        // given
        Long boardId = 1L;
        Long memberId = 2L; // 다른 회원의 ID
        BoardForm form = new BoardForm();
        form.setTitle("Updated Title");
        form.setContent("Updated Content");

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // when & then
        assertThrows(RuntimeException.class, () -> boardService.updateBoard(boardId, memberId, form));
    }

    @Test
    void deleteBoard_Success() {
        // given
        Long boardId = 1L;

        doNothing().when(boardRepository).deleteById(boardId);

        // when
        boardService.deleteById(boardId);

        // then
        verify(boardRepository, times(1)).deleteById(boardId);
    }

    @Test
    void findById_Success() {
        // given
        Long boardId = 1L;
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // when
        Board foundBoard = boardService.findById(boardId);

        // then
        assertNotNull(foundBoard);
        assertEquals(boardId, foundBoard.getId());
    }

    @Test
    void findById_NotFound() {
        // given
        Long boardId = 1L;
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> boardService.findById(boardId));
    }

    @Test
    void listBoards_Success() {
        // given
        when(boardRepository.findAll()).thenReturn(List.of(board));

        // when
        List<Board> boards = boardService.list();

        // then
        assertNotNull(boards);
        assertEquals(1, boards.size());
    }
}
