package dnd.studyplanner.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.goal.model.GoalStatus;
import dnd.studyplanner.domain.studygroup.model.StudyGroup;

public interface GoalRepository extends JpaRepository<Goal, Long> {

	Optional<Goal> findByGoalStatus(GoalStatus goalStatus);

	List<Goal> findByStudyGroup(StudyGroup studyGroup);

	Goal findFirstByStudyGroupOrderByGoalStartDateDesc(StudyGroup studyGroup);

	List<Goal> findAllByStudyGroupOrderByCreatedDateDesc(StudyGroup studyGroup);

	List<Goal> findAllByStudyGroupIdInOrderByGoalEndDate(List<Long> studyGroupIdList);
}
