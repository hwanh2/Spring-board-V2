//package study.spring_board_V2.repository;
//
//import jakarta.persistence.EntityManager;
//import org.springframework.stereotype.Repository;
//import study.spring_board_V2.domain.Board;
//import study.spring_board_V2.domain.Member;
//
//import java.util.List;
//
//@Repository
//public class JpaBoardRepository implements BoardRepository {
//
//    private final EntityManager em;
//    public JpaBoardRepository(EntityManager em) {
//        this.em = em;
//    }
//
//    @Override
//    public Board save(Board board) {
//        em.persist(board);
//        return board;
//    }
//
//    @Override
//    public List<Board> findAll() {
//        return em.createQuery("select b from Board b",Board.class)
//                .getResultList();
//    }
//
//    @Override
//    public List<Board> findByMember(Member member) {
//        return em.createQuery("select b from Board b where b.member = :member", Board.class)
//                .setParameter("member", member)
//                .getResultList();
//    }
//
//    @Override
//    public Board findById(Long id) {
//        Board board = em.find(Board.class,id);
//        return board;
//    }
//
//    // 게시글 삭제
//    @Override
//    public void deleteById(Long id) {
//        Board board = em.find(Board.class, id);
//        if (board != null) {
//            em.remove(board); // 해당 게시글 삭제
//        }
//    }
//}
