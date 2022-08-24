package dnd.studyplanner.dto.user.response.groupList;

import dnd.studyplanner.dto.goal.response.ActiveGoalResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupListGetResponse {

	private StudyGroupListResponse studyGroupListResponse;

	private ActiveGoalResponse activeGoalResponse;

	@Builder
	public StudyGroupListGetResponse(StudyGroupListResponse studyGroupListResponse, ActiveGoalResponse activeGoalResponse) {
		this.studyGroupListResponse = studyGroupListResponse;
		this.activeGoalResponse = activeGoalResponse;
	}

}
