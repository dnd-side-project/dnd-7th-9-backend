package dnd.studyplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.studyplanner.domain.user.model.UserSolveQuestion;

public interface UserSolveQuestionRepository extends JpaRepository<UserSolveQuestion, Long> {
	int countBySolveUser_IdAndAndSolveQuestionBook_IdAndRightCheck(Long userId, Long questionBookId, boolean right);

	List<UserSolveQuestion> findAllBySolveUserId(Long userId);

	List<UserSolveQuestion> findBySolveUserIdAndSolveQuestionBookId(Long userId, Long questionId);
}
