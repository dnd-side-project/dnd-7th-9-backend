package dnd.studyplanner.domain.user.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import dnd.studyplanner.domain.base.BaseEntity;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.dto.questionbook.response.UserQuestionBookResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserSolveQuestionBook extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_solve_question_book_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User solveUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_book_id")
	private QuestionBook solveQuestionBook;

	private boolean isSolved;

	private boolean isPassed;
	private int questionNumber;
	private int answerNum;

	@Builder
	public UserSolveQuestionBook(User solveUser, QuestionBook solveQuestionBook, int questionNumber) {
		this.solveUser = solveUser;
		this.solveQuestionBook = solveQuestionBook;
		this.questionNumber = questionNumber;
		this.isSolved = false;
		this.isPassed = false;
	}
}
