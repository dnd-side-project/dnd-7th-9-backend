package dnd.studyplanner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.user.model.UserGoalRate;

public interface UserGoalRateRepository extends JpaRepository<UserGoalRate, Long> {
	Optional<UserGoalRate> findByUser_IdAndGoal_Id(Long userId, Long goalId);
}
