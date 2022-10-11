package dnd.studyplanner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constant {

	public static String JWT_SECRET_KEY;

	public static String REFRESH_SECRET_KEY;

	public static String CLIENT_DOMAIN;
	public static String TEST_CLIENT_DOMAIN;

	public static final int JWT_EXPIRATION = 1000 * 60 * 60 * 24 * 7; //2시간 -> 1주
	public static final int REFRESH_EXPIRATION = 1000 * 60 * 60 * 24 * 7 * 2; //14일 (2주)
	public static final int WEEKS = 1000 * 60 * 60 * 24 * 7; // 7일

	public static final String KAKAO_ACCESS_TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";
	public static final String KAKAO_USER_INFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";

	//TODO : URL 반영
	public static final String GOOGLE_ACCESS_TOKEN_REQUEST_URL = "https://";
	public static final String NAVER_ACCESS_TOKEN_REQUEST_URL = "https://nid.naver.com/oauth2.0/token";
	public static final String NAVER_USER_INFO_REQUEST_URL = "https://openapi.naver.com/v1/nid/me";

	public Constant(
		@Value("${jwt.secret}") String jwtSecretKey,
		@Value("${jwt.refresh-secret}") String refreshSecretKey,
		@Value("${front-client.domain}") String clientDomain,
		@Value("${front-client.test-domain}") String testClientDomain
	) {
		JWT_SECRET_KEY = jwtSecretKey;
		REFRESH_SECRET_KEY = refreshSecretKey;
		CLIENT_DOMAIN = clientDomain;
		TEST_CLIENT_DOMAIN = testClientDomain;
	}
}
