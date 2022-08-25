package dnd.studyplanner.dto.user.response;

import java.util.List;

import dnd.studyplanner.dto.user.response.groupAndGoalDetail.StudyGroupDetailResponse;
import dnd.studyplanner.dto.user.response.versionDetail.StudyGroupAndGoalDetailPersonalVerResponse;
import dnd.studyplanner.dto.user.response.versionDetail.StudyGroupAndGoalDetailTeamVerResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStudyGroupListDetailResponse {

	private StudyGroupDetailResponse studyGroupDetailResponse;

	/**
	 * 개인 Ver 문제집 목록
	 */
	private List<StudyGroupAndGoalDetailPersonalVerResponse> studyGroupAndGoalDetailPersonalVerResponseList;

	/**
	 * 팀 Ver 문제집 목록
	 */
	private List<StudyGroupAndGoalDetailTeamVerResponse> studyGroupAndGoalDetailTeamVerResponseList;

	@Builder
	public UserStudyGroupListDetailResponse(StudyGroupDetailResponse studyGroupDetailResponse,
		List<StudyGroupAndGoalDetailPersonalVerResponse> studyGroupAndGoalDetailPersonalVerResponseList,
		List<StudyGroupAndGoalDetailTeamVerResponse> studyGroupAndGoalDetailTeamVerResponseList) {
		this.studyGroupDetailResponse = studyGroupDetailResponse;
		this.studyGroupAndGoalDetailPersonalVerResponseList = studyGroupAndGoalDetailPersonalVerResponseList;
		this.studyGroupAndGoalDetailTeamVerResponseList = studyGroupAndGoalDetailTeamVerResponseList;
	}
}
