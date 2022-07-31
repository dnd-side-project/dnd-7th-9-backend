package dnd.studyplanner.config;

import java.util.Collections;

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
import dnd.studyplanner.domain.member.model.Member;
import dnd.studyplanner.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private final MemberRepository memberRepository;
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
		// ex) Google, kakao, naver, ...
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();

		OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
		Member member = saveOrUpdate(attributes); // email로 Member 조회 후 업데이트 || 회원가입

		//로그인 시 토큰 생성
		saveOrUpdateAuthEntity(member);

		return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
			attributes.getAttributes(), attributes.getNameAttributeKey()); // 이 부분은 자세히 모르겠어요...
	}

	private Member saveOrUpdate(OAuthAttributes attributes) {
		Member member = memberRepository.findByEmail(attributes.getEmail())
			.orElse(attributes.toEntity());

		return memberRepository.save(member);
	}

	private void saveOrUpdateAuthEntity(Member member) {
		if (!authRepository.existsByMemberId(member.getId())) {
			String jwt = jwtService.createJwt(member.getId());
			String refreshToken = jwtService.createRefreshToken(member.getId());
			authRepository.save(new AuthEntity(jwt, refreshToken, member.getId()));
		}
	}

}