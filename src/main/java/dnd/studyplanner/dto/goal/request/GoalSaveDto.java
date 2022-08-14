package dnd.studyplanner.dto.goal.request;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalSaveDto {

    private Long studyGroupId;
    private String goalContent;
    private LocalDate goalStartDate;
    private LocalDate goalEndDate;
    private Long goalRegisterUserId;
    private Long goalUpdateUserId;

    public Goal toEntity(User user, StudyGroup studyGroup) {
        return Goal.builder()
                .studyGroup(studyGroup)
                .goalContent(this.goalContent)
                .goalStartDate(this.goalStartDate)
                .goalEndDate(this.goalEndDate)
                .goalRegisterUser(user)
                .goalUpdateUser(user)
                .build();
    }

}
