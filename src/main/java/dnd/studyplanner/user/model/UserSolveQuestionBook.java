package dnd.studyplanner.user.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import dnd.studyplanner.questionbook.model.QuestionBook;
import lombok.Getter;

@Getter
@Entity
public class UserSolveQuestionBook {

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

	private LocalDateTime solveDate;
	private int answerNum;
	private int answerRate;
}
