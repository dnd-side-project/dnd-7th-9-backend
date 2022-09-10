package dnd.studyplanner.dto.studyGroup.request;

import lombok.Getter;

@Getter
public class StudyGroupInviteDto {

    private String userEmail;

    private Long studyGroupId;
}
