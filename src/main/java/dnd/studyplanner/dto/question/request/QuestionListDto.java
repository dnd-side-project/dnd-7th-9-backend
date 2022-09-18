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

	private String questionContent;
	private String questionImage;
	private QuestionOptionType questionOptionType;

	private List<OptionSaveDto> optionSaveDtoList;

	@Builder
	public QuestionListDto(String questionContent, QuestionOptionType questionOptionType, List<OptionSaveDto> optionSaveDtoList, String questionImage) {
		this.questionContent = questionContent;
		this.questionOptionType = questionOptionType;
		this.optionSaveDtoList = optionSaveDtoList;
		this.questionImage = questionImage;
	}

	public Question toEntity(QuestionBook questionBook) {
		return Question.builder()
			.questionContent(this.questionContent)
			.questionBook(questionBook)
			.questionOptionType(this.questionOptionType)
			.questionImage(this.questionImage)
			.build();
	}
}
