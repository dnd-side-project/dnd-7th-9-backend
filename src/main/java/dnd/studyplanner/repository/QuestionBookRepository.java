package dnd.studyplanner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.domain.user.model.User;

public interface QuestionBookRepository extends JpaRepository<QuestionBook, Long> {

	Optional<QuestionBook> findByQuestionBookGoal_IdOrderByCreatedDateDesc(Long goalId);

	boolean existsByQuestionBookCreateUserAndQuestionBookGoal(User user, Goal goal);

}
