package dnd.studyplanner.dto.user.response.groupList;

import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.studygroup.model.StudyGroupCategory;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupListResponse {

    private Long groupId;
    private String groupName;
    private LocalDate groupStartDate;
    private LocalDate groupEndDate;
    private String groupGoal;
    private String groupImageUrl;
    private StudyGroupCategory groupCategory;
    private StudyGroupStatus groupStatus;


    @Builder
    public StudyGroupListResponse(StudyGroup studyGroup) {
        this.groupId = studyGroup.getId();
        this.groupName = studyGroup.getGroupName();
        this.groupStartDate = studyGroup.getGroupStartDate();
        this.groupEndDate = studyGroup.getGroupEndDate();
        this.groupGoal = studyGroup.getGroupGoal();
        this.groupImageUrl = studyGroup.getGroupImageUrl();
        this.groupCategory = studyGroup.getGroupCategory();
        this.groupStatus = studyGroup.getGroupStatus();

    }
}