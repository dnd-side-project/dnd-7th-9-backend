package dnd.studyplanner.config;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import dnd.studyplanner.auth.model.AuthEntity;
import dnd.studyplanner.auth.model.AuthRepository;
import dnd.studyplanner.config.dto.OAuthAttributes;
import dnd.studyplanner.member.model.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final MemberRepository memberRepository;
	private final AuthRepository authRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
		throws IOException, ServletException {

		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		Map<String, Object> attributes = oAuth2User.getAttributes();

		// registrationId 추출이 어려워 정규표현식으로 email 파싱 -> 서비스마다 email이 담겨있는 형식이 달라서..
		String email = findEmailByRegex(attributes.toString());

		log.debug("[USER EMAIL] : {}", email);

		Long memberId = memberRepository.findByEmail(email).get().getId();

		AuthEntity authEntity = authRepository.findById(memberId).get();
		String accessToken = authEntity.getJwt();
		String refreshToken = authEntity.getRefreshToken();

		// 최초 로그인이라면 회원가입 처리를 한다.
		String targetUrl;
		log.info("토큰 발행 시작");

		response.addHeader("access_token", accessToken);
		response.addHeader("refresh_token", refreshToken);
		targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/auth/after/login")
			.build().toUriString();
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	// 정규표현식을 통한 이메일 추출 메서드
	// is_email_valid=true, is_email_verified=true, email=testM@kakao.com}
	// return -> testM@kakao.com
	private String findEmailByRegex(String attributes) {
		Pattern p = Pattern.compile("([\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Za-z]{2,4})");
		Matcher m = p.matcher(attributes);

		while(m.find()) {
			if (m.group(1) != null) {
				break;
			};
		}

		return m.group(1);
	}
}