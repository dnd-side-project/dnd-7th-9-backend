package dnd.studyplanner.dto.questionbook.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SolveQuestionBookDto {
	private Long questionBookId;
}
