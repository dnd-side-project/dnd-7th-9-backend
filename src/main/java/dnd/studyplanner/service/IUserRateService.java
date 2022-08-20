package dnd.studyplanner.service;

import java.util.List;

import dnd.studyplanner.domain.user.model.UserGoalRate;

public interface IUserRateService {

	UserGoalRate updateAfterQuestionBook(UserGoalRate userGoalRate);

	UserGoalRate getUserGoalRateByQuestionBookId(String accessToken, Long questionId);

	UserGoalRate updatePostQuestionBook(String accessToken, Long goalId);

	int getUserStudyGroupRate(String accessToken, Long studyGroupId);

}
