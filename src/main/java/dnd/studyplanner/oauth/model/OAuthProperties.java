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
	@RequiredArgsConstructor
	public static final class KakaoOAuth {
		private final String clientId;
		private final String redirectUri;
		private final String authorizationGrantType;
		private final String clientAuthenticationMethod;
		private final List<String> scope;
	}

	@Getter
	@RequiredArgsConstructor
	public static final class NaverOAuth {
		private final String clientId;
		private final String redirectUri;
		private final String authorizationGrantType;
		private final String clientAuthenticationMethod;
		private final List<String> scope;
	}

	@Getter
	@RequiredArgsConstructor
	public static final class GoogleOAuth {
		private final String clientId;
		private final String redirectUri;
		private final String authorizationGrantType;
		private final String clientAuthenticationMethod;
		private final List<String> scope;
	}
}
