package study.spring_board_V2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.spring_board_V2.domain.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByName(String name);
}
