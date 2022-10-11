package dnd.studyplanner.oauth.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginSuccessResponseDto {
	private String accessToken;
	private String refreshToken;
	private String email;
	private Boolean isNewUser;

	@Builder
	public LoginSuccessResponseDto(String accessToken, String refreshToken, String email, Boolean isNewUser) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.email = email;
		this.isNewUser = isNewUser;
	}
}
