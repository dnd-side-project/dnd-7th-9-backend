package dnd.studyplanner.domain.question.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

import dnd.studyplanner.domain.base.BaseEntity;
import dnd.studyplanner.domain.option.model.Option;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.domain.user.model.UserSolveQuestion;
import dnd.studyplanner.dto.option.request.OptionSaveDto;
import dnd.studyplanner.dto.question.request.QuestionListDto;
import dnd.studyplanner.dto.question.response.QuestionResponseDto;
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
	private int answerCount = 0;
	private String questionImage;

	@Enumerated(EnumType.STRING)
	private QuestionOptionType questionOptionType;

	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
	private List<Option> options = new ArrayList<>();

	@OneToMany(mappedBy = "solveQuestion", cascade = CascadeType.ALL)
	private List<UserSolveQuestion> userSolveQuestions = new ArrayList<>();

	@Builder
	public Question(QuestionBook questionBook, String questionContent, QuestionOptionType questionOptionType, String questionImage) {
		this.questionBook = questionBook;
		this.questionContent = questionContent;
		this.questionOptionType = questionOptionType;
		this.questionImage = questionImage;

		questionBook.getQuestions().add(this);
	}

	public void countAnswer() {
		this.answerCount += 1;
	}

	public QuestionResponseDto toResponseDto() {
		return QuestionResponseDto.builder()
			.questionId(this.getId())
			.questionImage(this.questionImage)
			.questionContent(this.getQuestionContent())
			.optionList(this.getOptions().stream()
				.map(Option::toResponseDto)
				.collect(Collectors.toList()))
			.build();
	}

	public void updateByEditDto(QuestionListDto editQuestionDto) {
		this.questionContent = editQuestionDto.getQuestionContent();
		this.questionImage = editQuestionDto.getQuestionImage();
		this.questionOptionType = editQuestionDto.getQuestionOptionType();
	}

	public void clearOptions() {
		this.answerCount = 0;
		this.options.clear();
	}

}
