package dnd.studyplanner.oauth.dto.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthAccessTokenResponseDto {

	private String tokenType;
	private String accessToken;
	private int expiresIn;
	private String refreshToken;
	private int refreshTokenExpiresIn;
	private String scope;
}
