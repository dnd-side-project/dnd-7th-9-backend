package dnd.studyplanner.dto.studyGroup.response;

import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.studygroup.model.StudyGroupCategory;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AgreeInvitedStudyGroupResponse {

	private Long groupId;
	private String studyGroupName;   // 스터디 그룹 이름
	private StudyGroupCategory studyGroupCategory;
	private StudyGroupStatus studyGroupStatus;
	private String studyGroupGoal;   // 최종 목표 : 오픽 AL 달성

	@Builder
	public AgreeInvitedStudyGroupResponse(StudyGroup studyGroup) {
		this.groupId = studyGroup.getId();
		this.studyGroupName = studyGroup.getGroupName();
		this.studyGroupCategory = studyGroup.getGroupCategory();
		this.studyGroupStatus = studyGroup.getGroupStatus();
		this.studyGroupGoal = studyGroup.getGroupGoal();
	}
}
