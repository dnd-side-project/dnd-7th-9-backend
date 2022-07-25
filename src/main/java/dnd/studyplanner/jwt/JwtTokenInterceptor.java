package dnd.studyplanner.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

	private final JwtService jwtService;

	// Jwt Token Interceptor
	// WebMvcConfig 에서 설정한 uri 에 대해 Token 확인
	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) {

		log.debug("[JWT Token Interceptor]");
		String accessToken = jwtService.getJwt(); // Header 에 있는 Token 추출
		log.debug("[JWT] : {}", accessToken);
		String refreshToken = jwtService.getRefreshToken(); // Header 에 있는 Refresh Token 추출
		log.debug("[Refresh Token] : {}", refreshToken);

		if (accessToken == null) {
			log.warn("[JWT TOKEN EXCEPTION] : Token is not found");
			httpServletResponse.setStatus(401);
			httpServletResponse.setHeader("msg", "Check the tokens.");
			return false;

		} else {
			if (jwtService.isExpired(accessToken)) {     // jwt 토큰이 만료된 경우
				log.warn("[JWT TOKEN EXCEPTION] : {} is expired", accessToken);
				httpServletResponse.setStatus(401);
				httpServletResponse.setHeader("ACCESS_TOKEN", accessToken);
				httpServletResponse.setHeader("msg", "ACCESS TOKEN EXPIRED");
				return false;

			} else if (jwtService.isNotValid(accessToken)) {   // jwt 토큰이 유효하지 않은 경우
				log.warn("[JWT TOKEN EXCEPTION] : {} is invalid", accessToken);
				httpServletResponse.setStatus(409);
				httpServletResponse.setHeader("ACCESS_TOKEN", accessToken);
				httpServletResponse.setHeader("msg", "INVALID TOKEN");

				return false;
			}
			return true;
		}
	}
}
