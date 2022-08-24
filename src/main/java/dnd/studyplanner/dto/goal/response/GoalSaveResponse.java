package dnd.studyplanner.dto.goal.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.goal.model.GoalStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GoalSaveResponse {

	private Long studyGroupId;
	private String goalContent;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate goalStartDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate goalEndDate;

	private Long goalRegisterUserId;
	private Long goalUpdateUserId;
	private GoalStatus goalStatus;
	private int minQuestionPerQuestionBook;
	private int minSolveQuestionBook;
	private int minAnswerPerQuestionBook;
	private int minPersonPerQuestionBook;

	@Builder
	public GoalSaveResponse(Goal goal) {
		this.studyGroupId = goal.getStudyGroup().getId();
		this.goalContent = goal.getGoalContent();
		this.goalStartDate = goal.getGoalStartDate();
		this.goalEndDate = goal.getGoalEndDate();
		this.goalRegisterUserId = goal.getGoalRegisterUser().getId();
		this.goalUpdateUserId = goal.getGoalUpdateUser().getId();
		this.goalStatus = goal.getGoalStatus();
		this.minQuestionPerQuestionBook = goal.getMinQuestionPerQuestionBook();
		this.minSolveQuestionBook = goal.getMinSolveQuestionBook();
		this.minAnswerPerQuestionBook = goal.getMinAnswerPerQuestionBook();
		this.minPersonPerQuestionBook = goal.getMinPersonPerQuestionBook();
	}
}

