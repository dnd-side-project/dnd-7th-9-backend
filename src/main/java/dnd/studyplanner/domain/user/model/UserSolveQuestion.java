package dnd.studyplanner.domain.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
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

	private int pickOption;
	private int answerOption;
	private boolean rightCheck;

	@Builder
	public UserSolveQuestion(User solveUser, Question solveQuestion, int pickOption) {
		this.solveUser = solveUser;
		this.solveQuestion = solveQuestion;
		this.pickOption = pickOption;

		this.solveQuestionBook = solveQuestion.getQuestionBook();
		this.answerOption = solveQuestion.getQuestionAnswer();
		this.rightCheck = (this.answerOption == this.pickOption);
	}
}
