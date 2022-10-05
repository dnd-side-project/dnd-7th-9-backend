package dnd.studyplanner.oauth.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class OAuthPropertiesTest {
	@Autowired
	OAuthProperties oAuthProperties;

	@Test
	public void OAuthPropertiesBindingTest() {
		OAuthProperties.KakaoOAuth kakaoOAuth = oAuthProperties.getKakao();
		OAuthProperties.NaverOAuth naverOAuth = oAuthProperties.getNaver();
		OAuthProperties.GoogleOAuth googleOAuth = oAuthProperties.getGoogle();


		System.out.println("kakaoOAuth.getClientId() = " + kakaoOAuth.getClientId());
		System.out.println("naverOAuth.getClientId() = " + naverOAuth.getClientId());
		System.out.println("googleOAuth.getClientId() = " + googleOAuth.getClientId());
	}


}