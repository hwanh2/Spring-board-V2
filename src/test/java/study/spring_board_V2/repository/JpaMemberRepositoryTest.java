package study.spring_board_V2.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import study.spring_board_V2.domain.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest // JPA 관련 테스트 설정 (H2 자동 연결)
class JpaMemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @Test
    @Rollback(value = false)
    void save_and_findById() {
        // given
        Member member = new Member();
        member.setName("testUser");
        Member savedMember = memberRepository.save(member);

        // when
        Member foundMember = memberRepository.findById(savedMember.getId()).orElse(null);

        // then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getName()).isEqualTo("testUser");
    }

    @Test
    void findByName_whenMemberExists() {
        // given
        Member member = new Member();
        member.setName("duplicateName");
        memberRepository.save(member);

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

        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<Member> members = memberRepository.findAll();

        // then
        assertThat(members).hasSize(2);
        assertThat(members).extracting("name").containsExactlyInAnyOrder("User1", "User2");
    }
}
