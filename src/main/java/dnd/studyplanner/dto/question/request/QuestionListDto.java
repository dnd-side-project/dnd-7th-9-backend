package dnd.studyplanner.dto.question.request;

import java.util.ArrayList;
import java.util.List;

import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.question.model.QuestionOptionType;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.dto.option.request.OptionSaveDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionListDto {

	private int questionAnswer;
	private String questionContent;
	private QuestionOptionType questionOptionType;

	private List<OptionSaveDto> optionSaveDtoList;

	@Builder
	public QuestionListDto(int questionAnswer, String questionContent, QuestionOptionType questionOptionType, List<OptionSaveDto> optionSaveDtoList) {
		this.questionAnswer = questionAnswer;
		this.questionContent = questionContent;
		this.questionOptionType = questionOptionType;
		this.optionSaveDtoList = optionSaveDtoList;
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
