package study.spring_board_V2.repository;

import study.spring_board_V2.domain.Member;

import java.util.List;

public interface MemberRepository {
    Member save(Member member);
    Member findById(long id);
    Member findByName(String name);
    List<Member> findAll();
    Member merge(Member member);
}
