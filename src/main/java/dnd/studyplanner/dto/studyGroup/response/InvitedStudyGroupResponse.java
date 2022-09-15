package dnd.studyplanner.dto.studyGroup.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	private String studyGroupName;
	private StudyGroupCategory studyGroupCategory;
	private StudyGroupStatus studyGroupStatus;
	private String studyGroupGoal;   // 최종 목표 : 오픽 AL 달성

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate groupStartDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate groupEndDate;

	private int groupMemberNum;

	@Builder
	public InvitedStudyGroupResponse(StudyGroup studyGroup) {

		this.groupId = studyGroup.getId();
		this.studyGroupName = studyGroup.getGroupName();
		this.studyGroupCategory = studyGroup.getGroupCategory();
		this.studyGroupStatus = studyGroup.getGroupStatus();
		this.studyGroupGoal = studyGroup.getGroupGoal();
		this.groupStartDate = studyGroup.getGroupStartDate();
		this.groupEndDate = studyGroup.getGroupEndDate();
		this.groupMemberNum = studyGroup.getUserJoinGroups().size();

	}
}