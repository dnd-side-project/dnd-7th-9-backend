package dnd.studyplanner.domain.studygroup.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.domain.user.model.UserJoinGroup;
import lombok.Getter;

@Getter
@Entity
public class StudyGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "study_group_id")
	private Long id;

	private String groupName;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User groupCreateUser;

	private LocalDateTime groupCreateDate;
	private LocalDateTime groupStartDate;
	private String groupGoal;
	private String groupImageUrl;
	private String groupCategory;


	@OneToMany(mappedBy = "studyGroup")
	private List<UserJoinGroup> userJoinGroups = new ArrayList<>();

}
