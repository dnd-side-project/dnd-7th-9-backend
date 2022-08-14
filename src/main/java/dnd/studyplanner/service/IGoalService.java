package dnd.studyplanner.service;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.dto.goal.request.GoalSaveDto;

public interface IGoalService {

    Goal addPeriodGoal(String accessToken, GoalSaveDto goalSaveDto);
}
