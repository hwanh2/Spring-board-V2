package study.spring_board_V2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.spring_board_V2.domain.Comment;
import study.spring_board_V2.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Comment save(Comment comment){
        return commentRepository.Save(comment);
    }

    public Comment findById(Long id){
        return commentRepository.findById(id);
    }

    public List<Comment> findByBoardId(Long boardId){
        return commentRepository.findByBoardId(boardId);
    }

    public List<Comment> findByMemberId(Long memberId){
        return commentRepository.findByMemberId(memberId);
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
