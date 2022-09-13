package dnd.studyplanner.dto.studyGroup.response;


import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.studygroup.model.StudyGroupCategory;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class StudyGroupSaveResponse {

    private Long groupId;
    private String groupName;
    private LocalDate groupStartDate;
    private LocalDate groupEndDate;
    private String groupGoal;
    private String groupImageUrl;
    private StudyGroupCategory studyGroupCategory;
    private StudyGroupStatus studyGroupStatus;
    private List<String> studyGroupMember;

    private String invitedUrl;

    @Builder
    public StudyGroupSaveResponse(StudyGroup studyGroup, List<String> studyGroupMember, String invitedUrl) {

        this.groupId = studyGroup.getId();
        this.groupName = studyGroup.getGroupName();
        this.groupStartDate = studyGroup.getGroupStartDate();
        this.groupEndDate = studyGroup.getGroupEndDate();
        this.groupGoal = studyGroup.getGroupGoal();
        this.groupImageUrl = studyGroup.getGroupImageUrl();
        this.studyGroupCategory = studyGroup.getGroupCategory();
        this.studyGroupStatus = studyGroup.getGroupStatus();
        this.studyGroupMember = studyGroupMember;
        this.invitedUrl = invitedUrl;
    }
}
