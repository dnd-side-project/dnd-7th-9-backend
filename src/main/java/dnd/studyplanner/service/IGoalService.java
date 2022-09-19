package dnd.studyplanner.service;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.dto.goal.request.GoalSaveDto;
import dnd.studyplanner.dto.goal.response.GoalSaveResponse;
import dnd.studyplanner.exception.BaseException;

public interface IGoalService {

    GoalSaveResponse addDetailGoal(String accessToken, GoalSaveDto goalSaveDto) throws BaseException;
}
