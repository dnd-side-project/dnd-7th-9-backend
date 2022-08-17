package dnd.studyplanner.dto.questionbook.response;

import java.util.List;
import java.util.stream.Collectors;

import dnd.studyplanner.domain.option.model.Option;
import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.dto.option.response.OptionResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSolveQuestionResponse {

	private Long questionId;
	private String questionContent;
	private List<OptionResponseDto> optionList;

	private int pickOption;
	private int answerOption;
	private boolean rightCheck;

	@Builder
	public UserSolveQuestionResponse(Question question, int pickOption, int answerOption, boolean rightCheck) {
		this.questionId = question.getId();
		this.questionContent = question.getQuestionContent();
		this.optionList = question.getOptions().stream()
			.map(Option::toResponseDto)
			.collect(Collectors.toList());
		this.pickOption = pickOption;
		this.answerOption = answerOption;
		this.rightCheck = rightCheck;
	}
}
