package dnd.studyplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.studyplanner.domain.goal.model.Goal;

public interface GoalRepository extends JpaRepository<Goal, Long> {
}
