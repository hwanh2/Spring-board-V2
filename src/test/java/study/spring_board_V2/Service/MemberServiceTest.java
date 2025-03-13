package study.spring_board_V2.Service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.repository.MemberRepository;
import study.spring_board_V2.service.MemberService;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void join() {
        // given
        Member member = new Member();
        member.setName("hwanhee");
        member.setPassword("1234");

        // when
        Long saveId = memberService.join(member);

        // then
        Member findMember = memberService.findOne(saveId);
        Assertions.assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void SameNameTest() {
        // given
        Member member1 = new Member();
        member1.setName("test");
        member1.setPassword("password");

        Member member2 = new Member();
        member2.setName("test");
        member2.setPassword("password");

        // when
        memberService.join(member1);

        // then
        Assertions.assertThatThrownBy(() -> memberService.join(member2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 이름입니다.");
    }

    @Test
    public void signinTest() {
        // given
        Member member = new Member();
        member.setName("test");
        member.setPassword("password");

        // 회원가입
        memberService.join(member);

        // when
        Member foundMember = memberService.signin("test", "password");

        // then
        Assertions.assertThat(foundMember.getName()).isEqualTo("test");
        Assertions.assertThat(foundMember.getPassword()).isEqualTo("password");
    }

    @Test
    public void signinFailTest() {
        // given
        Member member = new Member();
        member.setName("test");
        member.setPassword("password");

        // 회원가입
        memberService.join(member);

        // when & then
        Assertions.assertThatThrownBy(() -> memberService.signin("test", "wrongpassword"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이름 또는 비밀번호가 잘못되었습니다.");
    }
}
