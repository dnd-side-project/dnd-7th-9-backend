package dnd.studyplanner.dto.oauth.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthAuthorizeCodeDto {
	//TODO Validation 필요
	private String authorizeCode;
}
