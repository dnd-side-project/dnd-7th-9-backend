package dnd.studyplanner.dto.questionbook.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionBookSaveResponse {
	private int addedRate; // 추가된 달성률
	private int userTotalRate; // 사용자의 전체 달성률
	private int questionBookPostRate;
	private int questionBookSolveRate;

	@Builder
	public QuestionBookSaveResponse(int addedRate, int userTotalRate, int questionBookPostRate,
		int questionBookSolveRate) {
		this.addedRate = addedRate;
		this.userTotalRate = userTotalRate;
		this.questionBookPostRate = questionBookPostRate;
		this.questionBookSolveRate = questionBookSolveRate;
	}
}
