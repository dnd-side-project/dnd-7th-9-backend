package dnd.studyplanner.dto.studyGroup.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import dnd.studyplanner.domain.user.model.User;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupSaveDto {

	private Long createUserId;
	private String groupName;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate groupStartDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate groupEndDate;

	private String groupGoal;
	private String groupImageUrl;
	private String groupCategory;

	@Setter
	private StudyGroupStatus groupStatus;

	@Builder
	public StudyGroupSaveDto(Long createUserId, String groupName, LocalDate groupStartDate, LocalDate groupEndDate,
							 String groupGoal, String groupImageUrl, String groupCategory, StudyGroupStatus groupStatus) {

		this.createUserId = createUserId;
		this.groupName = groupName;
		this.groupStartDate = groupStartDate;
		this.groupEndDate = groupEndDate;
		this.groupGoal = groupGoal;
		this.groupImageUrl = groupImageUrl;
		this.groupCategory = groupCategory;
		this.groupStatus = groupStatus;

	}

	public StudyGroup toEntity(User user) {
		return StudyGroup.builder()
			.groupCreateUser(user)
			.groupName(this.groupName)
			.groupStartDate(this.groupStartDate)
			.groupEndDate(this.groupEndDate)
			.groupGoal(this.groupGoal)
			.groupImageUrl(this.groupImageUrl)
			.groupCategory(this.groupCategory)
			.groupStatus(this.groupStatus)
			.build();
	}

}
