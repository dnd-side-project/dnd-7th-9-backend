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

    private List<MyStudyGroupResponse> studyGroupResponses;

    @Builder
    public MyStudyGroupPageResponse(String profileImageUrl, String nickname,
        List<MyStudyGroupResponse> studyGroupResponses) {
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.studyGroupResponses = studyGroupResponses;
    }
}
