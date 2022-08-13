package dnd.studyplanner.dto.response;


import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class StudyGroupSaveResponse {

    private StudyGroup newStudyGroup;

    private List<String> studyGroupMember;

    @Builder
    public StudyGroupSaveResponse(StudyGroup newStudyGroup, List<String> studyGroupMember) {
        this.newStudyGroup = newStudyGroup;
        this.studyGroupMember = studyGroupMember;
    }
}
