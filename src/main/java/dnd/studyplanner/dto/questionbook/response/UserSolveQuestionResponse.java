package dnd.studyplanner.dto.questionbook.response;

import java.util.List;
import java.util.stream.Collectors;

import dnd.studyplanner.domain.option.model.Option;
import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.dto.option.response.OptionResponseDto;
import dnd.studyplanner.dto.option.response.OptionSolvedDetailResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSolveQuestionResponse {

	private Long questionId;
	private String questionContent;
	private String questionImage;
	private List<OptionSolvedDetailResponseDto> optionList;

	private boolean rightCheck;

	@Builder
	public UserSolveQuestionResponse(Long questionId, String questionContent, String questionImage,
		List<OptionSolvedDetailResponseDto> optionList, boolean rightCheck) {
		this.questionId = questionId;
		this.questionContent = questionContent;
		this.questionImage = questionImage;
		this.optionList = optionList;
		this.rightCheck = rightCheck;
	}
}
