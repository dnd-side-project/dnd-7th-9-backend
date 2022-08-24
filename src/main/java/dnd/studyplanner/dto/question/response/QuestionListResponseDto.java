package dnd.studyplanner.dto.question.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionListResponseDto {

	private String goalContent;
	private String questionBookContent;

	private List<QuestionResponseDto> questionList;

	@Builder
	public QuestionListResponseDto(String goalContent, String questionBookContent,
		List<QuestionResponseDto> questionList) {
		this.goalContent = goalContent;
		this.questionBookContent = questionBookContent;
		this.questionList = questionList;
	}
}
