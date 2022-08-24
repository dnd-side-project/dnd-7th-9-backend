package dnd.studyplanner.jwt;

import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import dnd.studyplanner.dto.response.CustomResponse;
import dnd.studyplanner.dto.response.CustomResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

	private final JwtService jwtService;

	private final ObjectMapper objectMapper;
	// Jwt Token Interceptor
	// WebMvcConfig 에서 설정한 uri 에 대해 Token 확인
	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws
		IOException {

		log.debug("[JWT Token Interceptor]");
		String accessToken = jwtService.getJwt(); // Header 에 있는 Token 추출
		log.debug("[JWT] : {}", accessToken);

		if (accessToken == null) {
			log.warn("[JWT TOKEN EXCEPTION] : Token is not found");
			httpServletResponse.setStatus(401);
			getResponseMessage(httpServletResponse, TOKEN_NULL);
			return false;

		} else {
			if (jwtService.isExpired(accessToken)) {     // jwt 토큰이 만료된 경우
				log.warn("[JWT TOKEN EXPIRED] : {} is expired", accessToken);
				httpServletResponse.setStatus(401);
				httpServletResponse.setHeader("ACCESS_TOKEN", accessToken);
				getResponseMessage(httpServletResponse, TOKEN_EXPIRED);
				return false;

			} else if (jwtService.isNotValid(accessToken)) {   // jwt 토큰이 유효하지 않은 경우
				log.warn("[JWT TOKEN EXCEPTION] : {} is invalid", accessToken);
				httpServletResponse.setStatus(409);
				httpServletResponse.setHeader("ACCESS_TOKEN", accessToken);
				getResponseMessage(httpServletResponse, TOKEN_INVALID);

				return false;
			}
			return true;
		}
	}

	private void getResponseMessage(HttpServletResponse response, CustomResponseStatus status) throws IOException {
		PrintWriter writer = response.getWriter();
		objectMapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);
		String jsonMessage = objectMapper.writeValueAsString(new CustomResponse<>(status));
		writer.print(jsonMessage);
		writer.flush();
	}
}
