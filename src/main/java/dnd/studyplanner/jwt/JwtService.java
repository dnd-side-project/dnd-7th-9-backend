package dnd.studyplanner.jwt;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Service
public class JwtService {

	private static String JWT_SECRET_KEY;
	private static final int JWT_EXPIRATION = 1000 * 60 * 60 * 2; //2시간
	private static final int REFRESH_EXPIRATION = 1000 * 60 * 60 * 24 * 7 * 2 ; //14일 (2주)
	private static final int WEEKS = 1000 * 60 * 60 * 24 * 7; // 7일

	public JwtService(@Value("${jwt.secret}") String secretKey) {
		JWT_SECRET_KEY = secretKey;
	}

	// JWT 생성
	// by MemberId
	public String createJwt(Long memberId) {
		Date now = new Date();
		return Jwts.builder()
			.setHeaderParam("type", "jwt")
			.claim("memberId", memberId) //memberId로 저장
			.setIssuedAt(now)
			.setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION)) //
			.signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
			.compact();
	}

	public String createRefreshToken(Long memberId) {
		Date now = new Date();
		return Jwts.builder()
			.setHeaderParam("type", "jwt")
			.claim("memberId", memberId)
			.setIssuedAt(now)
			.setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
			.signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
			.compact();
	}

	public boolean isExpired(String jwt) {
		try {
			Claims claims = Jwts.parser()
				.setSigningKey(JWT_SECRET_KEY) //gitignore에 등록된 KEY
				.parseClaimsJws(jwt)
				.getBody();
			claims.getExpiration();
			return false;
		} catch (ExpiredJwtException e) {
			// 만료된 Jwt 토큰인 경우
			return true;
		}
	}

	public boolean isNotValid(String jwt) {
		try {
			Claims claims = Jwts.parser()
				.setSigningKey(JWT_SECRET_KEY) //gitignore에 등록된 KEY
				.parseClaimsJws(jwt)
				.getBody();
			claims.getExpiration();
			return false;
		}  catch (JwtException | NullPointerException exception) {
			return true;
		}
	}

	// Header에서 ACCESS-TOKEN에서 추출
	public String getJwt() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		return request.getHeader("Access-Token");
	}

	// Header에서 REFRESH-TOKEN 추출
	public String getRefreshToken() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		return request.getHeader("Refresh-Token");
	}

	public Long getMemberId(String accessToken) {

		//1. JWT 추출
		// String accessToken = getJwt();

		// 2. JWT parsing
		Jws<Claims> claims;
		claims = Jwts.parser()
			.setSigningKey(JWT_SECRET_KEY)
			.parseClaimsJws(accessToken);

		return claims.getBody().get("memberId", Long.class); //memberId 추출
	}


	// RefreshToken 만료 시간이 7일 남았으면,
	// 새로운 RefreshToken 발급
	public boolean isUpdatableRefreshToken (String refreshToken) {
		Date oneWeekLater = new Date(System.currentTimeMillis() + WEEKS);
		Date expiredAt = Jwts.parser()
			.setSigningKey(JWT_SECRET_KEY) //gitignore에 등록된 KEY
			.parseClaimsJws(refreshToken)
			.getBody()
			.getExpiration();

		if (expiredAt.before(oneWeekLater)) { // 만료기간이 7일 보다 안남았으면
			return true;
		}
		return false;
	}

}