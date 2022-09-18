package dnd.studyplanner.dto.goal.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.goal.model.GoalStatus;
import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalSaveDto {

    private Long studyGroupId;
    private String goalContent;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate goalStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate goalEndDate;

    private Long goalRegisterUserId;
    private Long goalUpdateUserId;

    @Setter
    private GoalStatus goalStatus;

    private int questionPerQuestionBook;
    private int minSolveQuestionBook;
    private int minAnswerPerQuestionBook;
    private int minPersonPerQuestionBook;

    public Goal toEntity(User user, StudyGroup studyGroup) {
        return Goal.builder()
                .studyGroup(studyGroup)
                .goalContent(this.goalContent)
                .goalStartDate(this.goalStartDate)
                .goalEndDate(this.goalEndDate)
                .goalRegisterUser(user)
                .goalUpdateUser(user)
                .goalStatus(this.goalStatus)
                .minQuestionPerQuestionBook(this.questionPerQuestionBook)
                .minSolveQuestionBook(this.minSolveQuestionBook)
                .minAnswerPerQuestionBook(this.minAnswerPerQuestionBook)
                .minPersonPerQuestionBook(this.minPersonPerQuestionBook)
                .build();
    }

}
