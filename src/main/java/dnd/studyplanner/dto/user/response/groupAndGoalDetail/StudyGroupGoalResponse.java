package dnd.studyplanner.dto.user.response.groupAndGoalDetail;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.user.model.UserGoalRate;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StudyGroupGoalResponse {

    private Long goalId;
    private String goalContent;
    private LocalDate goalStartDate;
    private LocalDate goalEndDate;
    private int achieveRate;

    @Builder
    public StudyGroupGoalResponse(Goal goal, int achieveRate) {
        this.goalId = goal.getId();
        this.goalContent = goal.getGoalContent();
        this.goalStartDate = goal.getGoalStartDate();
        this.goalEndDate = goal.getGoalEndDate();
        this.achieveRate = achieveRate;
    }
}
