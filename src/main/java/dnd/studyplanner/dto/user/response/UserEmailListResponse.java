package dnd.studyplanner.dto.user.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserEmailListResponse {

    List<String> userEmailList;

    @Builder
    public UserEmailListResponse(List<String> userEmailList) {
        this.userEmailList = userEmailList;
    }
}
