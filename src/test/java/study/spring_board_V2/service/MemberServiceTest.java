package study.spring_board_V2.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.dto.MemberForm;
import study.spring_board_V2.repository.MemberRepository;

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
        MemberForm form = new MemberForm();
        form.setName("hwanhee");
        form.setPassword("1234");

        // when
        Member savedMember = memberService.join(form); // MemberForm을 사용

        // then
        Member findMember = memberService.findOne(savedMember.getId());
        Assertions.assertThat(savedMember.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void SameNameTest() {
        // given
        MemberForm form1 = new MemberForm();
        form1.setName("test");
        form1.setPassword("password");

        MemberForm form2 = new MemberForm();
        form2.setName("test");
        form2.setPassword("password");

        // when
        memberService.join(form1);

        // then
        Assertions.assertThatThrownBy(() -> memberService.join(form2)) // MemberForm으로 변경
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 이름입니다.");
    }

    @Test
    public void signinTest() {
        // given
        MemberForm form = new MemberForm();
        form.setName("test");
        form.setPassword("password");

        // 회원가입
        memberService.join(form);

        // when
        Member foundMember = memberService.signin("test", "password");

        // then
        Assertions.assertThat(foundMember.getName()).isEqualTo("test");
        Assertions.assertThat(foundMember.getPassword()).isEqualTo("password");
    }

    @Test
    public void signinFailTest() {
        // given
        MemberForm form = new MemberForm();
        form.setName("test");
        form.setPassword("password");

        // 회원가입
        memberService.join(form);

        // when & then
        Assertions.assertThatThrownBy(() -> memberService.signin("test", "wrongpassword"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이름 또는 비밀번호가 잘못되었습니다.");
    }
}
