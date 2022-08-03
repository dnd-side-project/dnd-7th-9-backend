package dnd.studyplanner.dto.question.request;

import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.question.model.QuestionOptionType;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionSaveDto {
	private int questionAnswer;
	private String questionContent;
	private QuestionOptionType questionOptionType;
	private Long questionBookId;

	@Builder
	public QuestionSaveDto(int questionAnswer, String questionContent, QuestionOptionType questionOptionType, Long questionBookId) {
		this.questionAnswer = questionAnswer;
		this.questionContent = questionContent;
		this.questionOptionType = questionOptionType;
		this.questionBookId = questionBookId;
	}

	public Question toEntity(QuestionBook questionBook) {
		return Question.builder()
			.questionContent(this.questionContent)
			.questionAnswer(this.questionAnswer)
			.questionBook(questionBook)
			.questionOptionType(this.questionOptionType)
			.build();
	}

}
