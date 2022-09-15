package dnd.studyplanner.dto.studyGroup.response;

import dnd.studyplanner.domain.goal.model.GoalStatus;
import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.studygroup.model.StudyGroupCategory;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import dnd.studyplanner.dto.goal.response.ActiveGoalResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InvitedStudyGroupResponse {

	private Long groupId;
	private String studyGroupName;   // 스터디 그룹 이름
	private StudyGroupCategory studyGroupCategory;
	private StudyGroupStatus studyGroupStatus;
	private String studyGroupGoal;   // 최종 목표 : 오픽 AL 달성

	private GoalStatus goalStatus;   // 현재 날짜의 세부목표 진행 여부
	private int goalQuestionBookNum;   // 풀어야 할 전체 문제집 수
	private int goalSolveQuestionBookNum;   // 푼 문제집 수
	private boolean achieveGoalStatus;   // 세부 목표 달성 여부

	@Builder
	public InvitedStudyGroupResponse(StudyGroup studyGroup, ActiveGoalResponse activeGoalResponse) {

		this.groupId = studyGroup.getId();
		this.studyGroupName = studyGroup.getGroupName();
		this.studyGroupCategory = studyGroup.getGroupCategory();
		this.studyGroupStatus = studyGroup.getGroupStatus();
		this.studyGroupGoal = studyGroup.getGroupGoal();
		this.goalStatus = activeGoalResponse.getGoalStatus();

		this.goalQuestionBookNum = activeGoalResponse.getToSolveQuestionBookNum();
		this.goalSolveQuestionBookNum = activeGoalResponse.getClearSolveQuestionBookNum();
		this.achieveGoalStatus = activeGoalResponse.isAchieveGoalStatus();
	}
}