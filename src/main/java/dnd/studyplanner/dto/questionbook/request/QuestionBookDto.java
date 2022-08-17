package dnd.studyplanner.dto.questionbook.request;

import java.util.ArrayList;
import java.util.List;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.dto.question.request.QuestionListDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionBookDto {

	private Long goalId;
	private String questionBookName;
	private int questionBookQuestionNum;

	private List<QuestionListDto> questionDtoList = new ArrayList<>();



	@Builder
	public QuestionBookDto(Long goalId, String questionBookName, int questionBookQuestionNum, List<QuestionListDto> questionDtoList) {
		this.goalId = goalId;
		this.questionBookName = questionBookName;
		this.questionBookQuestionNum = questionBookQuestionNum;
		this.questionDtoList = questionDtoList;
	}

	public QuestionBook toEntity(Goal goal, User user) {
		return QuestionBook.builder()
			.questionBookGoal(goal)
			.questionBookCreateUser(user)
			.questionBookQuestionNum(this.questionBookQuestionNum)
			.questionBookName(this.questionBookName)
			.build();
	}
}
