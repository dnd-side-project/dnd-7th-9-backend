package dnd.studyplanner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.studyplanner.domain.member.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	 Optional<Member> findByEmail(String email);
}
