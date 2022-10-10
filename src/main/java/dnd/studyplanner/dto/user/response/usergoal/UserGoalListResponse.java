package dnd.studyplanner.dto.user.response.usergoal;

import java.util.List;

import dnd.studyplanner.dto.goal.response.ActiveGoalResponse;
import dnd.studyplanner.dto.user.response.groupList.StudyGroupListResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGoalListResponse {

	private String userNickname;
	private List<UserGoalResponse> userGoalResponseList;
	private List<StudyGroupResponse> emptyGoalStudyGroup;

	@Builder
	public UserGoalListResponse(String userNickname, List<UserGoalResponse> userGoalResponseList,
		List<StudyGroupResponse> emptyGoalStudyGroup) {
		this.userNickname = userNickname;
		this.userGoalResponseList = userGoalResponseList;
		this.emptyGoalStudyGroup = emptyGoalStudyGroup;
	}
}
