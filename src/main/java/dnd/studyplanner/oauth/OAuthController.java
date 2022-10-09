package dnd.studyplanner.oauth;

import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnd.studyplanner.auth.dto.AuthProvider;
import dnd.studyplanner.dto.oauth.request.OAuthAuthorizeCodeDto;
import dnd.studyplanner.dto.response.CustomResponse;
import dnd.studyplanner.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//FIXME : package 구조 변경 필요

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/oauth")
@RestController
public class OAuthController {

	private final OAuthServiceV2 oAuthService;

	@PostMapping("/login/{provider}")
	public ResponseEntity<CustomResponse> socialLogin(
		@RequestBody OAuthAuthorizeCodeDto requestDto,
		@PathVariable String provider
	) {
		try {
			AuthProvider authProvider = AuthProvider.valueOf(provider);
			log.info("[SOCIAL LOGIN] : Provider {}", authProvider);

			String email = oAuthService.oAuthLogin(requestDto.getAuthorizeCode(),
				authProvider);
			log.info(email);

			//TODO : 사용자 email을 통한 가입여부 확인
			//TODO : ResponseDto로 응답

			return null;
		} catch (IllegalArgumentException valueOfException) {
			return new CustomResponse<>(INVALID_PROVIDER).toResponseEntity();
		} catch (BaseException e) {
			return new CustomResponse<>(e.getStatus()).toResponseEntity();
		}

	}


}
