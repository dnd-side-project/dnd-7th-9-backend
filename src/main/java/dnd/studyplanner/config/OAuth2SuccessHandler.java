package dnd.studyplanner.config;

import static dnd.studyplanner.config.Constant.*;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dnd.studyplanner.auth.dto.AuthProvider;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import dnd.studyplanner.auth.model.AuthEntity;
import dnd.studyplanner.auth.model.AuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final UserRepository userRepository;
	private final AuthRepository authRepository;
	private final ObjectMapper objectMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication)
		throws IOException, ServletException {

		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		Map<String, Object> attributes = oAuth2User.getAttributes();

		// registrationId 추출이 어려워 정규표현식으로 email 파싱 -> 서비스마다 email이 담겨있는 형식이 달라서..
		String email = findEmailByRegex(attributes.toString());

		User user = userRepository.findByUserEmail(email).get();
		Long userId = user.getId();

		AuthEntity authEntity = authRepository.findById(userId).get();
		String accessToken = authEntity.getJwt();
		String refreshToken = authEntity.getRefreshToken();

		log.debug("[USER EMAIL] : {}", email);
		log.debug("[TOKEN] : {}", accessToken);
		log.debug("[REFRESH_TOKEN] : {}", refreshToken);



		String redirectDomain = getRedirectDomainByRequestURI(request.getRequestURI());

		String targetUrl = UriComponentsBuilder.fromUriString(redirectDomain + "/login")
			.queryParam("success", true)
			.queryParam("token", accessToken)
			.queryParam("refresh", refreshToken)
			.queryParam("email", user.getUserEmail())
			.queryParam("new-user", user.isNewUser())
			.build().toUriString();

		if (user.isNewUser()) {
			user.updateNewUser();
			userRepository.save(user);
		}


		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	// FIXME : 테스트 종료 후 없어질 Method
	private String getRedirectDomainByRequestURI(String requestURI) {
		String[] directories = requestURI.split("/");
		String providerInfo = directories[directories.length - 1];
		if (AuthProvider.valueOf(providerInfo) == AuthProvider.kakao) {
			return TEST_CLIENT_DOMAIN;
		}

		return CLIENT_DOMAIN;
	}

	// 정규표현식을 통한 이메일 추출 메서드
	// is_email_valid=true, is_email_verified=true, email=testM@kakao.com}
	// return -> testM@kakao.com
	private String findEmailByRegex(String attributes) {
		Pattern p = Pattern.compile("([\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Za-z]{2,4})");
		Matcher m = p.matcher(attributes);

		while (m.find()) {
			if (m.group(1) != null) {
				break;
			}
		}

		return m.group(1);
	}
}