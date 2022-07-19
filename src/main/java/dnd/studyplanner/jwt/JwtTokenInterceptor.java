package dnd.studyplanner.jwt;

import java.io.IOException;

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
	// WebMvcConfig에서 설정한 uri에 대해 Token 확인
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws IOException {

		log.debug("[JWT Token Interceptor]");
		String accessToken = jwtService.getJwt(); //Header에 있는 Token 추출
		log.debug("[JWT] : {}", accessToken);
		String refreshToken = jwtService.getRefreshToken(); //Header에 있는 Refresh Token 추출
		log.debug("[Refresh Token] : {}", refreshToken);

		if (accessToken != null) { //Token이 있고,
			if (jwtService.isExpired(accessToken)) { //JWT가 만료된 경우
				log.warn("[JWT TOKEN EXCEPTION] : {} is expired", accessToken);
				response.setStatus(401);
				response.setHeader("ACCESS_TOKEN", accessToken);
				response.setHeader("msg", "ACCESS TOKEN EXPIRED");
				return false;
			} else if (jwtService.isNotValid(accessToken)) { //옳바른 JWT가 아닌 경우
				log.warn("[JWT TOKEN EXCEPTION] : {} is invalid", accessToken);
				response.setStatus(401);
				response.setHeader("ACCESS_TOKEN", accessToken);
				response.setHeader("msg", "INVALID TOKEN");
				return false;
			}
			return true;
		}

		log.warn("[JWT TOKEN EXCEPTION] : {} is not found");
		response.setStatus(401);
		response.setHeader("ACCESS_TOKEN", accessToken);
		response.setHeader("REFRESH_TOKEN", refreshToken);
		response.setHeader("msg", "Check the tokens.");
		return false;
	}
}
