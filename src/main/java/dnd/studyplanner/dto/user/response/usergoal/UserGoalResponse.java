package dnd.studyplanner.dto.user.response.usergoal;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import dnd.studyplanner.domain.goal.model.GoalStatus;
import dnd.studyplanner.domain.studygroup.model.StudyGroupCategory;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGoalResponse {

	private Long studyGroupId;
	private Long goalId;
	private StudyGroupCategory studyGroupCategory;

	private StudyGroupStatus studyGroupStatus;
	private GoalStatus goalStatus;
	private String goalContent;
	private String studyGroupContent;

	private Boolean questionBookSubmitted;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate groupEndDate;

	@Builder
	public UserGoalResponse(Long studyGroupId, Long goalId, StudyGroupCategory studyGroupCategory,
		StudyGroupStatus studyGroupStatus, GoalStatus goalStatus, String goalContent, String studyGroupContent,
		Boolean questionBookSubmitted, LocalDate groupEndDate) {
		this.studyGroupId = studyGroupId;
		this.goalId = goalId;
		this.studyGroupCategory = studyGroupCategory;
		this.studyGroupStatus = studyGroupStatus;
		this.goalStatus = goalStatus;
		this.goalContent = goalContent;
		this.studyGroupContent = studyGroupContent;
		this.questionBookSubmitted = questionBookSubmitted;
		this.groupEndDate = groupEndDate;
	}
}
