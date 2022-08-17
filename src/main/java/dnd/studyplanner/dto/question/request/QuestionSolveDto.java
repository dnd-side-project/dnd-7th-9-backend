package dnd.studyplanner.dto.question.request;

import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.domain.user.model.UserSolveQuestion;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionSolveDto {
	private Long questionId;
	private int checkAnswer;

	@Builder
	public QuestionSolveDto(Long questionId, int checkAnswer) {
		this.questionId = questionId;
		this.checkAnswer = checkAnswer;
	}

	public UserSolveQuestion toEntity(User solveUser, Question solveQuestion) {
		return UserSolveQuestion.builder()
			.solveUser(solveUser)
			.solveQuestion(solveQuestion)
			.pickOption(this.checkAnswer)
			.build();
	}
}
