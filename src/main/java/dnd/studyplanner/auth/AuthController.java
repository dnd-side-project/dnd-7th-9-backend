package dnd.studyplanner.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import dnd.studyplanner.auth.dto.TokenResponseDto;
import dnd.studyplanner.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController("/auth")
public class AuthController {

	private final AuthService authService;

	@GetMapping("/token/reissue")
	public TokenResponseDto reissueAccessToken(
		@RequestHeader(value = "Refresh-Token") String refreshToken
	) {
		if (authService.isExpiredToken(refreshToken)) { // Refresh Token이 만료된 경우
			log.debug("[REFRESH TOKEN EXPIRED] : {} is expired. Please Login again", refreshToken);
			return null;
		}
		TokenResponseDto tokenResponseDto = authService.reissueAccessToken(refreshToken);
		log.debug("[NEW ACCESS TOKEN] : {}", tokenResponseDto.getAccessToken());
		return tokenResponseDto;
	}
}
