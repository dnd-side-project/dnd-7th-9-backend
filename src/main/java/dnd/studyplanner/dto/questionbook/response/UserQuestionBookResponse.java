package dnd.studyplanner.dto.questionbook.response;

import java.time.LocalDateTime;

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
public class UserQuestionBookResponse {

	private Long questionBookId;
	private String goalContent;
	private String userNickName;
	private String userProfileImageUrl;
	private String studyGroupName;
	private boolean isSolved;

	private LocalDateTime questionCreatedAt;

	@Builder
	public UserQuestionBookResponse(User user, QuestionBook questionBook, boolean isSolved) {
		this.questionBookId = questionBook.getId();
		this.goalContent = questionBook.getQuestionBookGoal().getGoalContent();
		this.userNickName = user.getUserNickName();
		this.userProfileImageUrl = user.getUserProfileImageUrl();
		this.studyGroupName = questionBook.getQuestionBookGoal().getStudyGroup().getGroupName();
		this.questionCreatedAt = questionBook.getCreatedDate();
		this.isSolved = isSolved;
	}
}
