package dnd.studyplanner.oauth.service;

import static dnd.studyplanner.config.Constant.*;
import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.oauth.dto.kakao.OAuthAccessTokenResponseDto;
import dnd.studyplanner.oauth.model.OAuthProperties;
import dnd.studyplanner.oauth.model.OAuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class KakaoOAuthLoginService extends OAuthLoginService {

	private final ObjectMapper objectMapper;
	private final OAuthProperties oAuthProperties;

	@Override
	public String requestAccessToken(String code) throws BaseException {
		OAuthProvider kakaoProperty = oAuthProperties.getKakao();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("grant_type", kakaoProperty.getAuthorizationGrantType());
			params.add("client_id", kakaoProperty.getClientId());
			params.add("redirect_uri", kakaoProperty.getRedirectUri());
			params.add("code", code);
			HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

			RestTemplate restTemplate = new RestTemplate();
			HttpMethod requestMethod = HttpMethod.valueOf(kakaoProperty.getClientAuthenticationMethod());

			ResponseEntity<String> response = restTemplate.exchange(
				KAKAO_ACCESS_TOKEN_REQUEST_URL, requestMethod, request,
				String.class);

			OAuthAccessTokenResponseDto oAuthAccessTokenResponseDto = objectMapper.readValue(
				response.getBody(),
				OAuthAccessTokenResponseDto.class
			);

			return oAuthAccessTokenResponseDto.getAccessToken();
		} catch (JsonProcessingException e) {
			throw new BaseException(INTERNAL_SERVER_ERROR);
		} catch (HttpClientErrorException e) {
			throw new BaseException(INVALID_AUTHORIZATION_CODE);
		}
	}

	public String getUserEmail(String accessToken) throws BaseException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Bearer " + accessToken);
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(KAKAO_USER_INFO_REQUEST_URL, HttpMethod.GET, requestEntity,
			String.class);

		return findEmailByRegex(response.getBody());
	}
}
