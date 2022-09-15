package dnd.studyplanner.domain.user.model;

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

import dnd.studyplanner.domain.option.model.Option;
import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.dto.questionbook.response.UserSolveQuestionResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserSolveQuestion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_solve_question_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User solveUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_book_id")
	private QuestionBook solveQuestionBook;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id")
	private Question solveQuestion;

	private int pickOption; //deprecate
	private int answerOption;
	private boolean rightCheck; // 정답 유무

	@OneToMany(mappedBy = "userSolveQuestion", cascade = CascadeType.ALL)
	private List<UserCheckOption> userCheckOptions = new ArrayList<>();

	@Builder
	public UserSolveQuestion(User solveUser, Question solveQuestion) {
		this.solveUser = solveUser;
		this.solveQuestion = solveQuestion;
		this.solveQuestionBook = solveQuestion.getQuestionBook();
		this.answerOption = solveQuestion.getQuestionAnswer();

		solveQuestion.getUserSolveQuestions().add(this);
	}

	public void setRightCheck(boolean rightCheck) {
		this.rightCheck = rightCheck;
	}


	public UserSolveQuestionResponse toResponseDto() {
		return UserSolveQuestionResponse.builder()
			.question(this.getSolveQuestion())
			.answerOption(this.answerOption)
			.pickOption(this.pickOption)
			.rightCheck(this.rightCheck)
			.build();
	}

}
