package dnd.studyplanner.domain.goal.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import dnd.studyplanner.domain.base.BaseEntity;
import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.domain.user.model.User;
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
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate goalStartDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate goalEndDate;

	@Enumerated(EnumType.STRING)
	private GoalStatus goalStatus;

	/**
	 * TODO : 문제집 당 문제 개수 fixed -> 최소 문제 X,  문제 O
	 * 		  RequestDto 에 포함되어있는 이름은 변경 완료
	 * 		  Entity 내 이름도 수정 필요
	 * 		  minQuestionPerQuestionBook -> questionPerQuestionBook
	 */

	private int questionPerQuestionBook;   // 문제집 당 최소 문제 수
	private int minSolveQuestionBook;   // 개인별 최소 풀이 문제집 수
	private int minAnswerPerQuestionBook;   // 한 문제집 당 최소 정답 수
	private int minPersonPerQuestionBook;   // 문제집 당 최소 팀원 참여 수 ?

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
				User goalRegisterUser, User goalUpdateUser, GoalStatus goalStatus,
				int questionPerQuestionBook, int minSolveQuestionBook, int minAnswerPerQuestionBook, int minPersonPerQuestionBook) {
		this.studyGroup = studyGroup;
		this.goalContent = goalContent;
		this.goalStartDate = goalStartDate;
		this.goalEndDate = goalEndDate;
		this.goalRegisterUser = goalRegisterUser;
		this.goalUpdateUser = goalUpdateUser;
		this.goalStatus = goalStatus;
		this.questionPerQuestionBook = questionPerQuestionBook;
		this.minSolveQuestionBook = minSolveQuestionBook;
		this.minAnswerPerQuestionBook = minAnswerPerQuestionBook;
		this.minPersonPerQuestionBook = minPersonPerQuestionBook;
	}

	public void update(String goalContent, LocalDate goalStartDate, LocalDate goalEndDate,
					   User goalUpdateUser, GoalStatus goalStatus) {
		this.goalContent = goalContent;
		this.goalStartDate = goalStartDate;
		this.goalEndDate = goalEndDate;
		this.goalUpdateUser = goalUpdateUser;
		this.goalStatus = goalStatus;
	}

	public void updateStatus(GoalStatus goalStatus) {
		this.goalStatus = goalStatus;
	}
}
