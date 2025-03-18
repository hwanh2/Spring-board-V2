//package study.spring_board_V2.repository;
//
//import jakarta.persistence.EntityManager;
//import org.springframework.stereotype.Repository;
//import study.spring_board_V2.domain.Comment;
//
//import java.util.List;
//
//@Repository
//public class JpaCommentRepository implements CommentRepository {
//    private final EntityManager em;
//    public JpaCommentRepository(EntityManager em) {
//        this.em = em;
//    }
//
//    @Override
//    public Comment Save(Comment comment) {
//        em.persist(comment);
//        return comment;
//    }
//
//    @Override
//    public Comment findById(Long id) {
//        return em.find(Comment.class,id);
//    }
//
//    @Override
//    public List<Comment> findByMemberId(Long memberId) {
//        List<Comment> result = em.createQuery("select c from Comment c where c.member.id = :memberId",Comment.class)
//                .setParameter("memberId",memberId)
//                .getResultList();
//        return result;
//    }
//
//    @Override
//    public List<Comment> findByBoardId(Long boardId) {
//        List<Comment> result = em.createQuery("select c from Comment c where c.board.id = :boardId",Comment.class)
//                .setParameter("boardId",boardId)
//                .getResultList();
//        return result;
//    }
//
//    @Override
//    public void deleteById(Long id) {
//        Comment comment = em.find(Comment.class,id);
//        if (comment != null) {
//            em.remove(comment);
//        }
//    }
//}
