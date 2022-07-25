package dnd.studyplanner.auth;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	private final JwtService jwtService;

	// Access-Token 이 만료되어
	// Refresh-Token 으로 재발급 요청
	@GetMapping("/token/reissue")
	public ResponseEntity<TokenResponseDto> reissueAccessToken(
		@RequestHeader(value = "Refresh-Token") String refreshToken
	) {
		HttpHeaders httpHeaders = new HttpHeaders();
		if (jwtService.isNotValidRefreshToken(refreshToken)) { // Refresh Token 이 유효하지 않은 경우
			log.debug("[REFRESH TOKEN EXPIRED] : {} is invalid refresh token", refreshToken);
			httpHeaders.set("msg", "Invalid Refresh Token");
			return ResponseEntity.status(HttpStatus.CONFLICT)
				.headers(httpHeaders)
				.body(null);
		}

		if (jwtService.isExpiredRefreshToken(refreshToken)) { // Refresh Token 이 만료된 경우
			log.debug("[REFRESH TOKEN EXPIRED] : {} is expired. Please login again", refreshToken);
			httpHeaders.set("msg", "Refresh Token is expired. Please login again");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.headers(httpHeaders)
				.body(null);
		}

		TokenResponseDto tokenResponseDto = authService.reissueAccessToken(refreshToken);
		log.debug("[NEW ACCESS TOKEN] : {}", tokenResponseDto.getAccessToken());
		return ResponseEntity
				.ok()
				.body(tokenResponseDto);
	}

	@GetMapping("/after/login")
	public void afterLoginTest( //login 여부 테스트 추후 삭제할 API
		@RequestParam(value = "success") boolean success,
		@RequestParam(value = "token", required = false) String token,
		@RequestParam(value = "refresh", required = false) String refresh
	) {
		if (success) {
			log.debug("[LOGIN RESULT] : {}", true);
			log.debug("[ACCESS TOKEN] : {}", token);
			log.debug("[REFRESH ACCESS TOKEN] : {}", refresh);
		} else {
			log.debug("[LOGIN RESULT] : {}", false);
		}

	}
}
