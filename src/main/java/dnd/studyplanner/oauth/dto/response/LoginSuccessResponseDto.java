package dnd.studyplanner.oauth.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginSuccessResponseDto {
	private String accessToken;
	private String refreshToken;
	private String email;
	private Boolean isNewUser;
}
