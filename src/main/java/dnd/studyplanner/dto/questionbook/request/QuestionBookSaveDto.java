package dnd.studyplanner.dto.questionbook.request;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.domain.user.model.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionBookSaveDto {
	private Long goalId;
	private Long createUserId;
	private String questionBookName;
	private int questionBookMinAchieveRate;
	private int questionBookQuestionNum;

	@Builder
	public QuestionBookSaveDto(Long goalId, Long createUserId, String questionBookName, int questionBookMinAchieveRate,
		int questionBookQuestionNum) {
		this.goalId = goalId;
		this.createUserId = createUserId;
		this.questionBookName = questionBookName;
		this.questionBookMinAchieveRate = questionBookMinAchieveRate;
		this.questionBookQuestionNum = questionBookQuestionNum;
	}

	public QuestionBook toEntity(Goal goal, User createUser) {
		return QuestionBook.builder()
			.build();
	}
}
