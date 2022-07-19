package dnd.studyplanner.auth.kakao.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class KakaoTokenInfo {
	private Long id;
	private Long expires_in;
	private Long app_id;
}
