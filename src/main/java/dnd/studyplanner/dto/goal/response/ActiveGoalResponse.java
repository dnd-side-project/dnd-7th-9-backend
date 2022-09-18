package dnd.studyplanner.dto.goal.response;

import java.time.LocalDate;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.goal.model.GoalStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ActiveGoalResponse {

	private String goalContent;

	private LocalDate goalStartDate;

	private LocalDate goalEndDate;

	private GoalStatus goalStatus;

	private boolean achieveGoalStatus;   // 세부 목표 달성 여부

	private boolean checkSubmitQuestionBook;   // 문제 제출 여부

	private boolean checkSolveQuestionBook;   // 문제 풀기 완료 여부

	private int clearSolveQuestionBookNum;   // 풀기를 완료한 문제집 개수

	private int toSolveQuestionBookNum;   // 풀어야 하는 문제집 개수

	private int questionPerQuestionBook; // 문제집 출제 시 풀어야하는 문제 개수

	@Builder
	public ActiveGoalResponse(Goal goal, boolean achieveGoalStatus, boolean checkSubmitQuestionBook, boolean checkSolveQuestionBook,
							  int clearSolveQuestionBookNum, int toSolveQuestionBookNum, int questionPerQuestionBook) {

		this.goalContent = goal.getGoalContent();
		this.goalStartDate = goal.getGoalStartDate();
		this.goalEndDate = goal.getGoalEndDate();
		this.goalStatus = goal.getGoalStatus();
		this.achieveGoalStatus = achieveGoalStatus;
		this.checkSubmitQuestionBook = checkSubmitQuestionBook;
		this.checkSolveQuestionBook = checkSolveQuestionBook;
		this.clearSolveQuestionBookNum = clearSolveQuestionBookNum;
		this.toSolveQuestionBookNum = toSolveQuestionBookNum;
		this.questionPerQuestionBook = questionPerQuestionBook;
	}

}
