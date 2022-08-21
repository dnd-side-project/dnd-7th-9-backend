package dnd.studyplanner.dto.studyGroup.request;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.studygroup.model.StudyGroupCategory;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import dnd.studyplanner.domain.user.model.User;
import lombok.*;

import javax.persistence.Enumerated;

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

	private StudyGroupCategory groupCategory;

	@Setter
	private StudyGroupStatus groupStatus;

	List<String> invitedUserEmailList;

	@Builder
	public StudyGroupSaveDto(Long createUserId, String groupName, LocalDate groupStartDate, LocalDate groupEndDate,
							 String groupGoal, String groupImageUrl, StudyGroupCategory groupCategory, StudyGroupStatus groupStatus) {

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
