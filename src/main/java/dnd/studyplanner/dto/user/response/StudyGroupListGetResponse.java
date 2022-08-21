package dnd.studyplanner.dto.user.response;

import dnd.studyplanner.dto.goal.response.ActiveGoalResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StudyGroupListGetResponse {

	private StudyGroupListResponse studyGroupListResponse;

	private ActiveGoalResponse activeGoalResponse;

	@Builder
	public StudyGroupListGetResponse(StudyGroupListResponse studyGroupListResponse, ActiveGoalResponse activeGoalResponse) {
		this.studyGroupListResponse = studyGroupListResponse;
		this.activeGoalResponse = activeGoalResponse;
	}

}
