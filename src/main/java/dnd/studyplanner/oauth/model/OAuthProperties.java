package dnd.studyplanner.oauth.model;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration")
public class OAuthProperties {
	private final KakaoOAuth kakao;
	private final NaverOAuth naver;
	private final GoogleOAuth google;

	@Getter
	public static final class KakaoOAuth extends OAuthProvider {
		public KakaoOAuth(
			String clientId,
			String redirectUri,
			String authorizationGrantType,
			String clientAuthenticationMethod,
			List<String> scope
		) {
			super(clientId, redirectUri, authorizationGrantType, clientAuthenticationMethod, scope);
		}
	}

	@Getter
	public static final class NaverOAuth extends OAuthProvider {
		public NaverOAuth(
			String clientId,
			String redirectUri,
			String authorizationGrantType,
			String clientAuthenticationMethod,
			List<String> scope
		) {
			super(clientId, redirectUri, authorizationGrantType, clientAuthenticationMethod, scope);
		}
	}

	@Getter
	public static final class GoogleOAuth extends OAuthProvider {
		public GoogleOAuth(
			String clientId,
			String redirectUri,
			String authorizationGrantType,
			String clientAuthenticationMethod,
			List<String> scope
		) {
			super(clientId, redirectUri, authorizationGrantType, clientAuthenticationMethod, scope);
		}
	}
}
