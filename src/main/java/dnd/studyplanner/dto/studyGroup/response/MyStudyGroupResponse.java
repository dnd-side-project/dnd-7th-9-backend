package dnd.studyplanner.dto.studyGroup.response;

import java.time.LocalDate;

import dnd.studyplanner.domain.studygroup.model.StudyGroupCategory;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyStudyGroupResponse{
    private Long groupId;
    private String groupName;
    private LocalDate groupStartDate;
    private LocalDate groupEndDate;
    private String groupGoal;
    private String groupImageUrl;
    private StudyGroupCategory groupCategory;
    private StudyGroupStatus groupStatus;
    private int studyGroupRate;

    @Builder
    public MyStudyGroupResponse(Long groupId, String groupName, LocalDate groupStartDate, LocalDate groupEndDate,
                                String groupGoal, String groupImageUrl, StudyGroupCategory groupCategory, StudyGroupStatus groupStatus,
                                int studyGroupRate) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupStartDate = groupStartDate;
        this.groupEndDate = groupEndDate;
        this.groupGoal = groupGoal;
        this.groupImageUrl = groupImageUrl;
        this.groupCategory = groupCategory;
        this.groupStatus = groupStatus;
        this.studyGroupRate = studyGroupRate;
    }
}