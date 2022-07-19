package dnd.studyplanner.jwt;

import static dnd.studyplanner.config.SecretConstant.*;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

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

	private static final int JWT_EXPIRATION = 1000 * 60 * 60 * 2;      //2시간
	private static final int REFRESH_EXPIRATION = 1000 * 60 * 60 * 24; //24시간

	// JWT 생성
	// by MemberId
	public String createJwt(Long memberId) {
		Date now = new Date();
		return Jwts.builder()
			.setHeaderParam("type", "jwt")
			.claim("memberId", memberId) //memberId로 저장
			.setIssuedAt(now)
			.setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION)) //
			.signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY) //gitignore에 등록된 KEY
			.compact();
	}

	public String createRefreshToken(Long memberId) {
		Date now = new Date();
		return Jwts.builder()
			.setHeaderParam("type", "jwt")
			.claim("memberId", memberId)
			.setIssuedAt(now)
			.setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
			.signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY) //gitignore에 등록된 KEY
			.compact();
	}

	public boolean isExpired(String jwt) {
		try {
			Claims claims = Jwts.parser()
				.setSigningKey(DatatypeConverter.parseBase64Binary(JWT_SECRET_KEY)) //gitignore에 등록된 KEY
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
				.setSigningKey(DatatypeConverter.parseBase64Binary(JWT_SECRET_KEY)) //gitignore에 등록된 KEY
				.parseClaimsJws(jwt)
				.getBody();
			claims.getExpiration();
			return false;
		}  catch (JwtException | NullPointerException exception) {
			return true;
		}
	}

	// Header : ACCESS-TOKEN에서 Jwt 추출
	public String getJwt() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		return request.getHeader("ACCESS-TOKEN");
	}

	// Header : REFRESH-TOKEN에서 Jwt 추출
	public String getRefreshToken() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		return request.getHeader("REFRESH-TOKEN");
	}

	public Long getMemberId() {

		//1. JWT 추출
		String accessToken = getJwt();

		// 2. JWT parsing
		Jws<Claims> claims;
		claims = Jwts.parser()
			.setSigningKey(JWT_SECRET_KEY)
			.parseClaimsJws(accessToken);

		return claims.getBody().get("memberId", Long.class); //memberId 추출
	}

}