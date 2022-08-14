package dnd.studyplanner.dto.questionbook.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionBookSolveResponse {
	private boolean isPass;
	private int addedRate; // 추가된 달성률
	private int userTotalRate; // 사용자의 전체 달성률
	private int questionBookPostRate; // 출제를 통해 얻은 달성률 (0% or 50%)
	private int questionBookSolveRate; // 풀이를 통해 얻은 달성률 (0% ~ 50%)

	@Builder
	public QuestionBookSolveResponse(boolean isPass, int addedRate, int userTotalRate, int questionBookPostRate,
		int questionBookSolveRate) {
		this.isPass = isPass;
		this.addedRate = addedRate;
		this.userTotalRate = userTotalRate;
		this.questionBookPostRate = questionBookPostRate;
		this.questionBookSolveRate = questionBookSolveRate;
	}
}
