package dnd.studyplanner.service;

import dnd.studyplanner.domain.user.model.UserGoalRate;

public interface IUserRateService {

	UserGoalRate updateAfterQuestionBook(String accessToken, Long questionId);

	UserGoalRate getUserGoalRateByQuestionBookId(String accessToken, Long questionId);

	void updatePostQuestionBook(String accessToken, Long goalId);
}
