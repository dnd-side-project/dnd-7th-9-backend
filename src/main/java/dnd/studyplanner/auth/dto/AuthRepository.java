package dnd.studyplanner.auth.dto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<AuthEntity, Long> {

	boolean existsByMemberId(Long memberId);
}