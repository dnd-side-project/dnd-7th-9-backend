package dnd.studyplanner.jwt;

import static dnd.studyplanner.config.SecretConstant.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import java.util.Date;

@Service
public class JwtService {

	/*
	JWT 생성
	@param userIdx
	@return String
	 */
	public String createJwt(Long memberId) {
		Date now = new Date();
		return Jwts.builder()
			.setHeaderParam("type", "jwt")
			.claim("memberId", memberId)
			.setIssuedAt(now)
			.setExpiration(new Date(System.currentTimeMillis() + 1 * (1000 * 60 * 5)))
			.signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
			.compact();
	}

	public String createRefreshToken(Long memberId) {
		Date now = new Date();
		return Jwts.builder()
			.setHeaderParam("type", "jwt")
			.claim("memberId", memberId)
			.setIssuedAt(now)
			.setExpiration(new Date(System.currentTimeMillis() + 1 * (1000 * 60 * 60)))
			.signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
			.compact();
	}

	public boolean isExpired(String jwt) {
		try {
			Claims claims = Jwts.parser()
				.setSigningKey(DatatypeConverter.parseBase64Binary(JWT_SECRET_KEY))
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
				.setSigningKey(DatatypeConverter.parseBase64Binary(JWT_SECRET_KEY))
				.parseClaimsJws(jwt)
				.getBody();
			claims.getExpiration();
			return false;
		}  catch (JwtException | NullPointerException exception) {
			return true;
		}
	}

	// Header : X-ACCESS-TOKEN에서 Jwt 추출
	public String getJwt() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		return request.getHeader("X-ACCESS-TOKEN");
	}

	public int getUserIdx() {

		//1. JWT 추출
		String accessToken = getJwt();

		//만료시 처리

		// if (accessToken == null || accessToken.length() == 0) {
		// 	throw new BaseException(EMPTY_JWT);
		// }

		// 2. JWT parsing
		Jws<Claims> claims;
		claims = Jwts.parser()
			.setSigningKey(JWT_SECRET_KEY)
			.parseClaimsJws(accessToken);

		// 3. userIdx 추출
		return claims.getBody().get("userIdx", Integer.class);  // jwt 에서 userIdx를 추출합니다.
	}

}