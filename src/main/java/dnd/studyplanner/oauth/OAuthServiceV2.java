package dnd.studyplanner.oauth;

import static dnd.studyplanner.config.Constant.*;
import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dnd.studyplanner.auth.dto.AuthProvider;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.oauth.dto.kakao.OAuthAccessTokenResponseDto;
import dnd.studyplanner.oauth.dto.response.LoginSuccessResponseDto;
import dnd.studyplanner.oauth.model.OAuthProperties;
import dnd.studyplanner.oauth.model.OAuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OAuthServiceV2 {

	private final ObjectMapper objectMapper;
	private final OAuthProperties oAuthProperties;

	public String oAuthLogin(String code, AuthProvider authProvider) throws BaseException {
		switch (authProvider) {
			case kakao:
				return requestToKakao(code);
			case naver:
				// return NAVER_ACCESS_TOKEN_REQUEST_URL;
			default:
				// return GOOGLE_ACCESS_TOKEN_REQUEST_URL;
		}
		return null;
	}

	private String requestToKakao(String code) throws BaseException {
		String accessToken = getAccessToken(code, KAKAO_ACCESS_TOKEN_REQUEST_URL, oAuthProperties.getKakao());
		return getUserEmail(accessToken, KAKAO_USER_INFO_REQUEST_URL);
	}

	private String getAccessToken(String code, String requestUrl, OAuthProvider oAuthProperties) throws BaseException {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("grant_type", oAuthProperties.getAuthorizationGrantType());
			params.add("client_id", oAuthProperties.getClientId());
			params.add("redirect_uri", oAuthProperties.getRedirectUri());
			params.add("code", code);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

			RestTemplate restTemplate = new RestTemplate();

			//TODO : GET METHOD로 요청하는 Provider에 대한 처리
			ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.POST, request,
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

	private String getUserEmail(String accessToken, String requestUrl) throws BaseException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Bearer " + accessToken);
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, requestEntity,
			String.class);

		return findEmailByRegex(response.getBody());
	}

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
