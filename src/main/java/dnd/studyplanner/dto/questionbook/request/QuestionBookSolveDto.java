package dnd.studyplanner.dto.questionbook.request;

import java.util.List;

import dnd.studyplanner.dto.question.request.QuestionSolveDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionBookSolveDto {
	private Long questionBookId;
	private List<QuestionSolveDto> solveDtoList;

	@Builder
	public QuestionBookSolveDto(Long questionBookId, List<QuestionSolveDto> solveDtoList) {
		this.questionBookId = questionBookId;
		this.solveDtoList = solveDtoList;
	}
}
