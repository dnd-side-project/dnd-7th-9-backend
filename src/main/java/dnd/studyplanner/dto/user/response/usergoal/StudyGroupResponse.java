package dnd.studyplanner.dto.user.response.usergoal;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import dnd.studyplanner.domain.studygroup.model.StudyGroupCategory;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupResponse {

	private StudyGroupCategory studyGroupCategory;
	private StudyGroupStatus studyGroupStatus;
	private String studyGroupContent;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate studyGroupEndDate;

	@Builder
	public StudyGroupResponse(StudyGroupCategory studyGroupCategory, StudyGroupStatus studyGroupStatus,
		String studyGroupContent, LocalDate studyGroupEndDate) {
		this.studyGroupCategory = studyGroupCategory;
		this.studyGroupStatus = studyGroupStatus;
		this.studyGroupContent = studyGroupContent;
		this.studyGroupEndDate = studyGroupEndDate;
	}
}
