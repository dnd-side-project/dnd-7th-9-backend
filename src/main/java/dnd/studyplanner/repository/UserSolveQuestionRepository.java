package dnd.studyplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.studyplanner.domain.user.model.UserSolveQuestion;

public interface UserSolveQuestionRepository extends JpaRepository<UserSolveQuestion, Long> {
}
