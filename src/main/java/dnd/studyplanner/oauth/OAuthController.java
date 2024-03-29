package dnd.studyplanner.oauth;

import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import dnd.studyplanner.auth.dto.AuthProvider;
import dnd.studyplanner.dto.oauth.request.OAuthAuthorizeCodeDto;
import dnd.studyplanner.dto.response.CustomResponse;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.oauth.dto.response.LoginSuccessResponseDto;
import dnd.studyplanner.oauth.service.OAuthLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//FIXME : package 구조 변경 필요

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/oauth")
@RestController
public class OAuthController {

	private final OAuthLoginService kakaoOAuthLoginService;
	private final OAuthLoginService naverOAuthLoginService;
	private final OAuthLoginService googleOAuthLoginService;

	@PostMapping("/login/{provider}")
	public ResponseEntity<CustomResponse> socialLogin(
		@RequestBody OAuthAuthorizeCodeDto requestDto,
		@PathVariable String provider
	) {
		try {
			AuthProvider authProvider = AuthProvider.valueOf(provider.toLowerCase());
			log.info("[SOCIAL LOGIN] : Provider {}", authProvider);

			OAuthLoginService oAuthLoginService = null;
			switch (authProvider) {
				case kakao:
					oAuthLoginService = kakaoOAuthLoginService;
					break;
				case naver:
					oAuthLoginService = naverOAuthLoginService;
					break;
				case google:
					oAuthLoginService = googleOAuthLoginService;
					break;
				default:
					return new CustomResponse<>(INVALID_PROVIDER).toResponseEntity();
			}

			String accessToken = oAuthLoginService.requestAccessToken(requestDto.getAuthorizeCode());
			String userEmail = oAuthLoginService.getUserEmail(accessToken);
			log.info(userEmail);

			LoginSuccessResponseDto response = oAuthLoginService.logInByEmail(userEmail);

			return new CustomResponse<>(response).toResponseEntity();
		} catch (IllegalArgumentException valueOfException) {
			return new CustomResponse<>(INVALID_PROVIDER).toResponseEntity();
		} catch (BaseException e) {
			return new CustomResponse<>(e.getStatus()).toResponseEntity();
		} catch (HttpClientErrorException httpClientErrorException) {
			return new CustomResponse<>(INVALID_AUTHORIZATION_CODE).toResponseEntity();
		}

	}


}
