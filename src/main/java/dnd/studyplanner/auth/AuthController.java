package dnd.studyplanner.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dnd.studyplanner.auth.dto.TokenResponseDto;
import dnd.studyplanner.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

	private final AuthService authService;

	// Access-Token 이 만료되어
	// Refresh-Token 으로 재발급 요청
	@GetMapping("/token/reissue")
	public TokenResponseDto reissueAccessToken(
		@RequestHeader(value = "Refresh-Token") String refreshToken
	) {
		if (authService.isExpiredToken(refreshToken)) { // Refresh Token이 만료된 경우
			log.debug("[REFRESH TOKEN EXPIRED] : {} is expired. Please Login again", refreshToken);
			return null; // Response 에 대한 규격을 정하고, ERROR CODE 추가해서 응답하면 좋을 것 같습니다!
		}
		TokenResponseDto tokenResponseDto = authService.reissueAccessToken(refreshToken);
		log.debug("[NEW ACCESS TOKEN] : {}", tokenResponseDto.getAccessToken());
		return tokenResponseDto;
	}

	@GetMapping("/after/login")
	public void afterLoginTest(
		@RequestHeader(value = "access_token") String access_token,
		@RequestHeader(value = "refresh_token") String refresh_token
	) {
		log.debug("[ACCESS TOKEN] : {}", access_token);
		log.debug("[REFRESH ACCESS TOKEN] : {}", refresh_token);
	}

}
