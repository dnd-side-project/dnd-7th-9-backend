package dnd.studyplanner.domain.goal.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import dnd.studyplanner.domain.base.BaseEntity;
import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.domain.user.model.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Goal extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "goal_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_group_id")
	private StudyGroup studyGroup;

	private String goalContent;

	private LocalDate goalStartDate;
	private LocalDate goalEndDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private User goalRegisterUser;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private User goalUpdateUser;

	@OneToMany(mappedBy = "questionBookGoal", cascade = CascadeType.ALL)
	private List<QuestionBook> questionBooks = new ArrayList<>();

	@Builder
	public Goal(StudyGroup studyGroup, String goalContent, LocalDate goalStartDate, LocalDate goalEndDate,
				User goalRegisterUser, User goalUpdateUser) {
		this.studyGroup = studyGroup;
		this.goalContent = goalContent;
		this.goalStartDate = goalStartDate;
		this.goalEndDate = goalEndDate;
		this.goalRegisterUser = goalRegisterUser;
		this.goalUpdateUser = goalUpdateUser;
	}

	public void update(String goalContent, LocalDate goalStartDate, LocalDate goalEndDate, User goalUpdateUser) {
		this.goalContent = goalContent;
		this.goalStartDate = goalStartDate;
		this.goalEndDate = goalEndDate;
		this.goalUpdateUser = goalUpdateUser;
	}
}
