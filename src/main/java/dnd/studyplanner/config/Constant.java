package dnd.studyplanner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constant {

	public static String JWT_SECRET_KEY;

	public static String REFRESH_SECRET_KEY;

	public static String CLIENT_DOMAIN;

	public static final int JWT_EXPIRATION = 1000 * 60 * 60 * 24 * 7; //2시간 -> 1주
	public static final int REFRESH_EXPIRATION = 1000 * 60 * 60 * 24 * 7 * 2; //14일 (2주)
	public static final int WEEKS = 1000 * 60 * 60 * 24 * 7; // 7일

	public Constant(
		@Value("${jwt.secret}") String jwtSecretKey,
		@Value("${jwt.refresh-secret}") String refreshSecretKey,
		@Value("${front-client.domain}") String clientDomain
	) {
		JWT_SECRET_KEY = jwtSecretKey;
		REFRESH_SECRET_KEY = refreshSecretKey;
		CLIENT_DOMAIN = clientDomain;
	}
}
