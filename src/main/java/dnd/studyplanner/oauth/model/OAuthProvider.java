package dnd.studyplanner.oauth.model;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuthProvider {
	private final String clientId;
	private final String clientSecret;
	private final String redirectUri;
	private final String authorizationGrantType;
	private final String clientAuthenticationMethod;
	private final List<String> scope;
}
