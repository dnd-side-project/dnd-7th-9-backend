package dnd.studyplanner.dto.studyGroup.response;

import java.time.LocalDate;
import java.util.List;

import dnd.studyplanner.domain.studygroup.model.StudyGroupCategory;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyStudyGroupPageResponse {
    private String profileImageUrl;
    private String nickname;
    private Integer activeStudyGroupCount;
    private Integer completeStudyGroupCount;

    private List<MyStudyGroupResponse> activeStudyGroupResponses;
    private List<MyStudyGroupResponse> completeStudyGroupResponses;

    @Builder
    public MyStudyGroupPageResponse(String profileImageUrl, String nickname, Integer activeStudyGroupCount,
        Integer completeStudyGroupCount,
        List<MyStudyGroupResponse> activeStudyGroupResponses,
        List<MyStudyGroupResponse> completeStudyGroupResponses) {
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.activeStudyGroupCount = activeStudyGroupCount;
        this.completeStudyGroupCount = completeStudyGroupCount;
        this.activeStudyGroupResponses = activeStudyGroupResponses;
        this.completeStudyGroupResponses = completeStudyGroupResponses;
    }
}
