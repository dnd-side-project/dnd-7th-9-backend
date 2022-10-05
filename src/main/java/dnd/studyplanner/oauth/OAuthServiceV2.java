package dnd.studyplanner.oauth;

import static dnd.studyplanner.config.Constant.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dnd.studyplanner.auth.dto.AuthProvider;
import dnd.studyplanner.oauth.dto.kakao.KakaoAccessTokenDto;
import dnd.studyplanner.oauth.model.OAuthProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OAuthServiceV2 {

	private final ObjectMapper objectMapper;
	private final OAuthProperties oAuthProperties;


	public String getAccessToken(String code, AuthProvider authProvider) {
		switch (authProvider) {
			case kakao:
				return requestToKakao(code);
			case naver:
				return NAVER_ACCESS_TOKEN_REQUEST_URL;
			default:
				return GOOGLE_ACCESS_TOKEN_REQUEST_URL;
		}
	}

	private String requestToKakao(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		OAuthProperties.KakaoOAuth kakaoProperties = oAuthProperties.getKakao();

		LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", kakaoProperties.getAuthorizationGrantType());
		params.add("client_id", kakaoProperties.getClientId());
		params.add("redirect_uri", kakaoProperties.getRedirectUri());
		params.add("code", code);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.postForEntity(KAKAO_ACCESS_TOKEN_REQUEST_URL, request, String.class);

		try {
			KakaoAccessTokenDto kakaoAccessTokenDto = objectMapper.readValue(
				response.getBody(),
				KakaoAccessTokenDto.class
			);
			return kakaoAccessTokenDto.getAccessToken();
		} catch (JsonProcessingException e) { //TODO : 인가코드에 대한 예외 처리
			e.printStackTrace();
		}

		return null;
	}
}
