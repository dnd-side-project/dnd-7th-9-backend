package dnd.studyplanner.domain.goal.model;

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

import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.domain.user.model.User;
import lombok.Getter;

@Getter
@Entity
public class Goal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "goal_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_group_id")
	private StudyGroup studyGroup;

	private String goalContent;
	private LocalDateTime goalStartDate;
	private LocalDateTime goalEndDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private User goalRegisterUser;

	private LocalDateTime goalRegisterDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private User goalUpdateUser;

	private LocalDateTime goalUpdateDate;

	@OneToMany(mappedBy = "questionBookGoal", cascade = CascadeType.ALL)
	private List<QuestionBook> questionBooks = new ArrayList<>();
}
