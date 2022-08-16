package dnd.studyplanner.dto.studyGroup.response;

import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.domain.user.model.UserJoinGroup;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupListResponse {

    private Long groupId;
    private String groupName;
    private LocalDate groupStartDate;
    private LocalDate groupEndDate;
    private String groupGoal;
    private String groupImageUrl;
    private String groupCategory;
    private StudyGroupStatus groupStatus;

    @Builder
    public StudyGroupListResponse(Long groupId, String groupName, LocalDate groupStartDate, LocalDate groupEndDate,
                                  String groupGoal, String groupImageUrl, String groupCategory, StudyGroupStatus groupStatus) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupStartDate = groupStartDate;
        this.groupEndDate = groupEndDate;
        this.groupGoal = groupGoal;
        this.groupImageUrl = groupImageUrl;
        this.groupCategory = groupCategory;
        this.groupStatus = groupStatus;

    }
}
