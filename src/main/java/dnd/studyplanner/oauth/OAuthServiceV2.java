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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dnd.studyplanner.auth.dto.AuthProvider;
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
public class OAuthServiceV2 {

	private final ObjectMapper objectMapper;
	private final OAuthProperties oAuthProperties;

	public String oAuthLogin(String code, AuthProvider authProvider) throws BaseException {
		switch (authProvider) {
			case kakao:
				return requestToKakao(code);
			case naver:
				return requestToNaver(code);
			default:
				// return GOOGLE_ACCESS_TOKEN_REQUEST_URL;
		}
		return null;
	}

	private String requestToKakao(String code) throws BaseException {
		String accessToken = getAccessToken(code, oAuthProperties.getKakao());
		return getUserEmail(accessToken, KAKAO_USER_INFO_REQUEST_URL);
	}

	private String requestToNaver(String code) throws BaseException {
		String accessToken = getAccessTokenNaver(code, oAuthProperties.getNaver());
		return getUserEmail(accessToken, NAVER_USER_INFO_REQUEST_URL);
	}

	private String getAccessToken(String code, OAuthProvider oAuthProperties) throws
		BaseException {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("grant_type", oAuthProperties.getAuthorizationGrantType());
			params.add("client_id", oAuthProperties.getClientId());
			params.add("redirect_uri", oAuthProperties.getRedirectUri());
			params.add("code", code);
			HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

			RestTemplate restTemplate = new RestTemplate();
			HttpMethod requestMethod = HttpMethod.valueOf(oAuthProperties.getClientAuthenticationMethod());

			ResponseEntity<String> response = restTemplate.exchange(
				dnd.studyplanner.config.Constant.KAKAO_ACCESS_TOKEN_REQUEST_URL, requestMethod, request,
				String.class);

			log.info(response.getBody());
			OAuthAccessTokenResponseDto oAuthAccessTokenResponseDto = objectMapper.readValue(
				response.getBody(),
				OAuthAccessTokenResponseDto.class
			);

			log.info(oAuthAccessTokenResponseDto.toString());

			return oAuthAccessTokenResponseDto.getAccessToken();
		} catch (JsonProcessingException e) {
			throw new BaseException(INTERNAL_SERVER_ERROR);
		} catch (HttpClientErrorException e) {
			throw new BaseException(INVALID_AUTHORIZATION_CODE);
		}
	}

	private String getAccessTokenNaver(String code, OAuthProvider oAuthProperties) throws
		BaseException {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("grant_type", oAuthProperties.getAuthorizationGrantType());
			params.add("client_id", oAuthProperties.getClientId());
			params.add("client_secret", oAuthProperties.getClientSecret());
			params.add("redirect_uri", oAuthProperties.getRedirectUri());
			params.add("code", code);
			HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

			RestTemplate restTemplate = new RestTemplate();
			HttpMethod requestMethod = HttpMethod.valueOf(oAuthProperties.getClientAuthenticationMethod());

			ResponseEntity<String> response = restTemplate.exchange(
				dnd.studyplanner.config.Constant.NAVER_ACCESS_TOKEN_REQUEST_URL, requestMethod, request,
				String.class);

			log.info(response.getBody());
			OAuthAccessTokenResponseDto oAuthAccessTokenResponseDto = objectMapper.readValue(
				response.getBody(),
				OAuthAccessTokenResponseDto.class
			);

			log.info(oAuthAccessTokenResponseDto.toString());

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

		log.info(accessToken);
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

	private String toLowerSnakeCase(String camelCase) {
		String regex = "([a-z])([A-Z]+)";
		String replacement = "$1_$2";
		return camelCase.replaceAll(regex, replacement).toLowerCase();
	}
}
