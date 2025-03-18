//package study.spring_board_V2.repository;
//
//import jakarta.persistence.EntityManager;
//import org.springframework.stereotype.Repository;
//import study.spring_board_V2.domain.Member;
//
//import java.util.List;
//
//@Repository
//public class JpaMemberRepository implements MemberRepository {
//    EntityManager em;
//
//    public JpaMemberRepository(EntityManager em) {
//        this.em = em;
//    }
//
//
//    @Override
//    public Member save(Member member) {
//        em.persist(member);
//        return member;
//    }
//
//    @Override
//    public Member findById(long id) {
//        Member member = em.find(Member.class,id);
//        return member;
//    }
//
//    @Override
//    public Member findByName(String name) {
//        List<Member> result = em.createQuery("select m from Member m where name=:name",Member.class)
//                .setParameter("name",name)
//                .getResultList();
//
//        return result.isEmpty() ? null : result.get(0);
//    }
//
//    @Override
//    public List<Member> findAll() {
//        return em.createQuery("select m from Member m",Member.class)
//                .getResultList();
//    }
//
//    @Override
//    public Member merge(Member member){
//        return em.merge(member);
//    }
//}
