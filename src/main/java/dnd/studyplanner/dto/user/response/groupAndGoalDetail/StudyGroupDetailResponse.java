package dnd.studyplanner.dto.user.response.groupAndGoalDetail;

import java.time.LocalDate;
import java.util.List;

import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.studygroup.model.StudyGroupCategory;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class StudyGroupDetailResponse {

	/**
	 * 스터디 그룹 정보
	 */
	private Long groupId;
	private String groupName;
	private String createUserName;   // 팀장
//	@Setter
	private List<String> invitedUserNameList;
	private LocalDate groupStartDate;
	private LocalDate groupEndDate;
	private String groupGoal;
	private String groupImageUrl;
	private StudyGroupCategory groupCategory;
	private StudyGroupStatus groupStatus;

	/**
	 * 세부 목표 정보
	 */
	List<StudyGroupGoalResponse> studyGroupGoalResponseList;

	@Builder
	public StudyGroupDetailResponse(StudyGroup studyGroup, List<String> invitedUserNameList, List<StudyGroupGoalResponse> studyGroupGoalResponseList) {
		this.groupId = studyGroup.getId();
		this.groupName = studyGroup.getGroupName();
		this.createUserName = studyGroup.getGroupCreateUser().getUserNickName();
		this.invitedUserNameList = invitedUserNameList;   // 추가
		this.groupStartDate = studyGroup.getGroupStartDate();
		this.groupEndDate = studyGroup.getGroupEndDate();
		this.groupGoal = studyGroup.getGroupGoal();
		this.groupImageUrl = studyGroup.getGroupImageUrl();
		this.groupCategory = studyGroup.getGroupCategory();
		this.groupStatus = studyGroup.getGroupStatus();
		this.studyGroupGoalResponseList = studyGroupGoalResponseList;

	}

}
