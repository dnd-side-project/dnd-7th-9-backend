package dnd.studyplanner.jwt;

import static dnd.studyplanner.config.Constant.*;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

	// JWT 생성
	// by userId
	public String createJwt(Long userId) {
		Date now = new Date();
		return Jwts.builder()
			.setHeaderParam("type", "jwt")
			.claim("userId", userId) //userId로 저장
			.setIssuedAt(now)
			.setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION)) //
			.signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
			.compact();
	}

	public String createRefreshToken(Long userId) {
		Date now = new Date();
		return Jwts.builder()
			.setHeaderParam("type", "refresh")
			.claim("userId", userId)
			.setIssuedAt(now)
			.setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
			.signWith(SignatureAlgorithm.HS256, REFRESH_SECRET_KEY)
			.compact();
	}

	public boolean isExpired(String jwt) {
		try {
			Claims claims = Jwts.parser()
				.setSigningKey(JWT_SECRET_KEY)
				.parseClaimsJws(jwt)
				.getBody();
			claims.getExpiration();
			return false;
		} catch (ExpiredJwtException e) {
			// 만료된 Jwt 토큰인 경우
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	public boolean isExpiredRefreshToken(String refresh) {
		try {
			Claims claims = Jwts.parser()
				.setSigningKey(REFRESH_SECRET_KEY)
				.parseClaimsJws(refresh)
				.getBody();
			claims.getExpiration();
			return false;
		} catch (ExpiredJwtException e) {
			// 만료된 Jwt 토큰인 경우
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	public boolean isNotValid(String jwt) {
		try {
			Long userId = Jwts.parser()
				.setSigningKey(JWT_SECRET_KEY) //gitignore에 등록된 KEY
				.parseClaimsJws(jwt)
				.getBody()
				.get("userId", Long.class);
			return false;
		} catch (JwtException | NullPointerException exception) {
			return true;
		}
	}

	public boolean isNotValidRefreshToken(String refresh) {
		try {
			Long userId = Jwts.parser()
				.setSigningKey(REFRESH_SECRET_KEY) //gitignore에 등록된 KEY
				.parseClaimsJws(refresh)
				.getBody()
				.get("userId", Long.class);
			return false;
		} catch (NullPointerException exception) {
			return true;
		} catch (ExpiredJwtException exception) {
			return false;
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

	public Long getUserId(String accessToken) {

		//1. JWT 추출
		// String accessToken = getJwt();

		// 2. JWT parsing
		Jws<Claims> claims;
		claims = Jwts.parser()
			.setSigningKey(JWT_SECRET_KEY)
			.parseClaimsJws(accessToken);

		return claims.getBody().get("userId", Long.class); //userId 추출
	}

	public Long getUserIdFromRefresh(String refreshToken) {

		//1. JWT 추출
		// String accessToken = getJwt();

		// 2. JWT parsing
		Jws<Claims> claims;
		claims = Jwts.parser()
			.setSigningKey(REFRESH_SECRET_KEY)
			.parseClaimsJws(refreshToken);

		return claims.getBody().get("userId", Long.class); //userId 추출
	}

	// RefreshToken 만료 시간이 7일 남았으면,
	// 새로운 RefreshToken 발급
	public boolean isUpdatableRefreshToken(String refreshToken) {
		Date oneWeekLater = new Date(System.currentTimeMillis() + WEEKS);
		Date expiredAt = Jwts.parser()
			.setSigningKey(REFRESH_SECRET_KEY) //gitignore에 등록된 KEY
			.parseClaimsJws(refreshToken)
			.getBody()
			.getExpiration();

		if (expiredAt.before(oneWeekLater)) { // 만료기간이 7일 보다 안남았으면
			return true;
		}
		return false;
	}

}