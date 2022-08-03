package dnd.studyplanner.domain.questionbook.model;

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
import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.domain.user.model.UserSolveQuestion;
import dnd.studyplanner.domain.user.model.UserSolveQuestionBook;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class QuestionBook extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "question_book_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "goal_id")
	private Goal questionBookGoal;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User questionBookCreateUser;

	private String questionBookName;
	private int questionBookMinAchieveRate;
	private int questionBookQuestionNum;

	@OneToMany(mappedBy = "questionBook", cascade = CascadeType.ALL)
	private List<Question> questions = new ArrayList<>();

	@OneToMany(mappedBy = "solveQuestionBook", cascade = CascadeType.ALL)
	private List<UserSolveQuestionBook> userSolveQuestionBooks = new ArrayList<>();

	@OneToMany(mappedBy = "solveQuestionBook", cascade = CascadeType.ALL)
	private List<UserSolveQuestion> userSolveQuestions = new ArrayList<>();

	@Builder
	public QuestionBook(Goal questionBookGoal, User questionBookCreateUser, String questionBookName,
		int questionBookMinAchieveRate, int questionBookQuestionNum) {
		this.questionBookGoal = questionBookGoal;
		this.questionBookCreateUser = questionBookCreateUser;
		this.questionBookName = questionBookName;
		this.questionBookMinAchieveRate = questionBookMinAchieveRate;
		this.questionBookQuestionNum = questionBookQuestionNum;

		questionBookGoal.getQuestionBooks().add(this);
	}
}
