package dnd.studyplanner.dto.questionbook.response;

import java.util.List;

import dnd.studyplanner.dto.question.response.QuestionResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionBookGetResponse {
	private Long questionBookId;
	private String questionBookContent;
	private List<QuestionResponseDto> questionResponseDtoList;

}
