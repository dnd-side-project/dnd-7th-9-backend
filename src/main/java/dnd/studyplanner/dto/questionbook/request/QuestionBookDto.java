package dnd.studyplanner.dto.questionbook.request;

import java.util.ArrayList;
import java.util.List;

import dnd.studyplanner.dto.question.request.QuestionListDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionBookDto {

	private Long goalId;
	private Long createUserId;
	private String questionBookName;
	private int questionBookMinAchieveRate;
	private int questionBookQuestionNum;

	private List<QuestionListDto> questionDtoList = new ArrayList<>();

}
