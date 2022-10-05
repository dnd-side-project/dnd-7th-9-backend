package dnd.studyplanner.oauth;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnd.studyplanner.auth.dto.AuthProvider;
import dnd.studyplanner.dto.oauth.request.OAuthAuthorizeCodeDto;
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
	public void socialLogin(
		@RequestBody OAuthAuthorizeCodeDto requestDto,
		@PathVariable String provider
	) {
		log.info("[SOCIAL LOGIN] : Provider {}", provider);
		String accessToken = oAuthService.getAccessToken(requestDto.getAuthorizeCode(), AuthProvider.valueOf(provider));

	}


}
