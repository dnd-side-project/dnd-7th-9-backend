package dnd.studyplanner.auth.kakao.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class KaKaoResponseDto {
	private String token_type;
	private String access_token;
	private Long expires_in;
	private String refresh_token;
	private Long refresh_token_expires_in;
	private String scope;
}
