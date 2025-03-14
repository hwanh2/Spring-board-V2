package study.spring_board_V2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.spring_board_V2.domain.Member;
import study.spring_board_V2.repository.MemberRepository;

import java.util.List;

@Service
public class MemberService {
    MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long join(Member member) {
        // 이름 중복 체크
        Member findMember = memberRepository.findByName(member.getName());
        if (findMember != null) {
            throw new IllegalStateException("이미 존재하는 이름입니다.");
        }
        // 회원 저장
        memberRepository.save(member);
        return member.getId();
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

    public Member findOne(long id){
        return memberRepository.findById(id);
    }

}
