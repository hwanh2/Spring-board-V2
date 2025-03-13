package study.spring_board_V2.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import study.spring_board_V2.domain.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest // JPA 관련 테스트에 특화된 설정 (H2 자동 연결)
class JpaMemberRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    private JpaMemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new JpaMemberRepository(em);
    }

    @AfterEach
    void tearDown() {
        em.createQuery("DELETE FROM Member").executeUpdate(); // 테스트 후 데이터 삭제
    }

    @Test
    @Rollback(value = false) // 트랜잭션 자동 롤백 방지
    void save_and_findById() {
        // given
        Member member = new Member();
        member.setName("testUser");
        memberRepository.Save(member);
        em.flush(); // 영속성 컨텍스트 반영

        // when
        Member foundMember = memberRepository.findById(member.getId());

        // then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getName()).isEqualTo("testUser");
    }

    @Test
    void findByName_whenMemberExists() {
        // given
        Member member = new Member();
        member.setName("duplicateName");
        memberRepository.Save(member);
        em.flush();

        // when
        Member foundMember = memberRepository.findByName("duplicateName");

        // then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getName()).isEqualTo("duplicateName");
    }

    @Test
    void findByName_whenMemberDoesNotExist() {
        // when
        Member foundMember = memberRepository.findByName("nonExistingName");

        // then
        assertThat(foundMember).isNull();
    }

    @Test
    void findAll() {
        // given
        Member member1 = new Member();
        member1.setName("User1");
        Member member2 = new Member();
        member2.setName("User2");

        memberRepository.Save(member1);
        memberRepository.Save(member2);
        em.flush();

        // when
        List<Member> members = memberRepository.findAll();

        // then
        assertThat(members).hasSize(2);
        assertThat(members).extracting("name").containsExactlyInAnyOrder("User1", "User2");
    }
}
