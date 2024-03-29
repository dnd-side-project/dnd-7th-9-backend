package dnd.studyplanner.config;

import java.util.Collections;
import java.util.Optional;

import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import dnd.studyplanner.auth.model.AuthEntity;
import dnd.studyplanner.auth.model.AuthRepository;
import dnd.studyplanner.config.dto.OAuthAttributes;
import dnd.studyplanner.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final AuthRepository authRepository;

	// OAuth2UserService
	// Application.yml 의 설정 값에 맞게
	// OAuth 요청 -> 인가 코드 발급 -> Access Token 발급 -> 사용자 정보 요청 과정이 이루어짐
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		log.debug("[OAuthUserRequest AccessToken] : {}", userRequest.getAccessToken().getTokenValue());
		log.debug("[OAuthUserRequest] : {}", userRequest.getAdditionalParameters());
		log.debug("[OAuthUserRequest] : {}", oAuth2User.getAttributes());


		// Social Service
		// ex) Google, Kakao, Naver
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();

		OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
		User user = saveOrUpdate(attributes); // email로 Member 조회 후 업데이트 || 회원가입

		//로그인 시 토큰 생성
		saveOrUpdateAuthEntity(user);

		return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
			attributes.getAttributes(), attributes.getNameAttributeKey()); // 이 부분은 자세히 모르겠어요...
	}

	private User saveOrUpdate(OAuthAttributes attributes) {
		User user = userRepository.findByUserEmail(attributes.getEmail())
			.orElse(attributes.toEntity());

		return userRepository.save(user);
	}

	private void saveOrUpdateAuthEntity(User user) {
		AuthEntity authEntity = authRepository.findById(user.getId()).orElse(
			AuthEntity.builder()
				.userId(user.getId())
				.build()
		);

		String jwt = jwtService.createJwt(user.getId());
		String refreshToken = jwtService.createRefreshToken(user.getId());
		authEntity.updateTokens(jwt, refreshToken);
		authRepository.save(authEntity);
	}

}