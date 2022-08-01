package dnd.studyplanner.domain.question.model;

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

import dnd.studyplanner.domain.base.BaseEntity;
import dnd.studyplanner.domain.option.model.Option;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.domain.user.model.UserSolveQuestion;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Question extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "question_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_book_id")
	private QuestionBook questionBook;

	private String questionContent;
	private int questionAnswer;
	private String questionOptionType;

	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
	private List<Option> options = new ArrayList<>();

	@OneToMany(mappedBy = "solveQuestion", cascade = CascadeType.ALL)
	private List<UserSolveQuestion> userSolveQuestions = new ArrayList<>();

	@Builder
	public Question(QuestionBook questionBook, String questionContent, int questionAnswer, String questionOptionType) {
		this.questionBook = questionBook;
		this.questionContent = questionContent;
		this.questionAnswer = questionAnswer;
		this.questionOptionType = questionOptionType;

		questionBook.getQuestions().add(this);
	}
}
