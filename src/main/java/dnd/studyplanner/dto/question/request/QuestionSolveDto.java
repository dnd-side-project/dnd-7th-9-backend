package dnd.studyplanner.dto.question.request;

import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.domain.user.model.UserSolveQuestion;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionSolveDto {
	private Long questionId;
	private int checkAnswer;

	public UserSolveQuestion toEntity(User solveUser, Question solveQuestion) {
		return UserSolveQuestion.builder()
			.solveUser(solveUser)
			.solveQuestion(solveQuestion)
			.pickOption(this.checkAnswer)
			.build();
	}
}
