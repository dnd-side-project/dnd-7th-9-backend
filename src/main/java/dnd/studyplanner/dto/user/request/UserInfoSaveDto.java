package dnd.studyplanner.dto.user.request;

import dnd.studyplanner.domain.user.model.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoSaveDto {

    private Long id;
    private String userName;
    private int userAge;
    private String userGender;
    private String userRegion;
    private String userProfileImageUrl;

    @Builder
    public UserInfoSaveDto(Long id, String userName, int userAge, String userGender, String userRegion, String userProfileImageUrl) {
        this.id = id;
        this.userName = userName;
        this.userAge = userAge;
        this.userGender = userGender;
        this.userRegion = userRegion;
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public User toEntity() {
        return User.builder()
            .build();
    }

}
