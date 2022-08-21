package dnd.studyplanner.domain.studygroup.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import dnd.studyplanner.domain.base.BaseEntity;
import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.domain.user.model.UserJoinGroup;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class StudyGroup extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "study_group_id")
	private Long id;

	private String groupName;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User groupCreateUser;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate groupStartDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate groupEndDate;
	private String groupGoal;
	private String groupImageUrl;

	@Enumerated(EnumType.STRING)
	private StudyGroupCategory groupCategory;

	@Enumerated(EnumType.STRING)
	private StudyGroupStatus groupStatus;

	@OneToMany(mappedBy = "studyGroup")
	private List<UserJoinGroup> userJoinGroups = new ArrayList<>();

	@OneToMany(mappedBy = "studyGroup")
	private List<Goal> groupDetailGoals = new ArrayList<>();

	@Builder
	public StudyGroup(String groupName, User groupCreateUser, LocalDate groupStartDate, LocalDate groupEndDate,
		String groupGoal, String groupImageUrl, StudyGroupCategory groupCategory, StudyGroupStatus groupStatus) {
		this.groupName = groupName;
		this.groupCreateUser = groupCreateUser;
		this.groupStartDate = groupStartDate;
		this.groupEndDate = groupEndDate;
		this.groupGoal = groupGoal;
		this.groupImageUrl = groupImageUrl;
		this.groupCategory = groupCategory;
		this.groupStatus = groupStatus;
	}

	public void update(String groupName, User groupCreateUser, LocalDate groupStartDate, LocalDate groupEndDate,
		String groupGoal, String groupImageUrl, StudyGroupCategory groupCategory, StudyGroupStatus groupStatus) {

		this.groupName = groupName;
		this.groupCreateUser = groupCreateUser;
		this.groupStartDate = groupStartDate;
		this.groupEndDate = groupEndDate;
		this.groupGoal = groupGoal;
		this.groupImageUrl = groupImageUrl;
		this.groupCategory = groupCategory;
		this.groupStatus = groupStatus;

	}
}
