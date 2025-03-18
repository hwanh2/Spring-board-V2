package study.spring_board_V2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.dto.MemberForm;
import study.spring_board_V2.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Member join(MemberForm form) {
        // 이름 중복 체크
        Member findMember = memberRepository.findByName(form.getName());
        if (findMember != null) {
            throw new IllegalStateException("이미 존재하는 이름입니다.");
        }

        // 새로운 회원 객체 생성 및 저장
        Member member = new Member();
        member.setName(form.getName());
        member.setPassword(form.getPassword());
        memberRepository.save(member);

        return member;
    }

    public Member signin(String name,String password){
        Member member = memberRepository.findByName(name);
        if(member==null || !member.getPassword().equals(password)){
            throw new IllegalStateException("이름 또는 비밀번호가 잘못되었습니다.");
        }
        return member;
    }

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(long id) {
        return memberRepository.findById(id).orElseThrow(() -> new IllegalStateException("회원이 존재하지 않습니다."));
    }

}
