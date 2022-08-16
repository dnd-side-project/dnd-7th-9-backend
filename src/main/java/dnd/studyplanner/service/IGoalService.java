package dnd.studyplanner.service;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.dto.goal.request.GoalSaveDto;

public interface IGoalService {

    Goal addDetailGoal(String accessToken, GoalSaveDto goalSaveDto);
}
