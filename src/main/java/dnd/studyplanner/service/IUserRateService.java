package dnd.studyplanner.service;

import dnd.studyplanner.domain.user.model.UserGoalRate;

public interface IUserRateService {

	UserGoalRate updateAfterQuestionBook(UserGoalRate userGoalRate);

	UserGoalRate getUserGoalRateByQuestionBookId(String accessToken, Long questionId);

	UserGoalRate updatePostQuestionBook(String accessToken, Long goalId);
}
