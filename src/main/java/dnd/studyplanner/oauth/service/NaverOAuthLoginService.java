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

import dnd.studyplanner.auth.model.AuthRepository;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.oauth.dto.kakao.OAuthAccessTokenResponseDto;
import dnd.studyplanner.oauth.model.OAuthProperties;
import dnd.studyplanner.oauth.model.OAuthProvider;
import dnd.studyplanner.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
@Service
public class NaverOAuthLoginService extends OAuthLoginService {

	public NaverOAuthLoginService(
		ObjectMapper objectMapper,
		OAuthProperties oAuthProperties,
		UserRepository userRepository,
		AuthRepository authRepository
	) {
		super(objectMapper, oAuthProperties, userRepository, authRepository);
	}

	@Override
	public String requestAccessToken(String code) throws BaseException {
		OAuthProvider naverProperty = oAuthProperties.getNaver();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("code", code);
			params.add("grant_type", naverProperty.getAuthorizationGrantType());
			params.add("client_id", naverProperty.getClientId());
			params.add("client_secret", naverProperty.getClientSecret());
			params.add("redirect_uri", naverProperty.getRedirectUri());
			HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

			RestTemplate restTemplate = new RestTemplate();
			HttpMethod requestMethod = HttpMethod.valueOf(naverProperty.getClientAuthenticationMethod());

			ResponseEntity<String> response = restTemplate.exchange(
				NAVER_ACCESS_TOKEN_REQUEST_URL, requestMethod, request,
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

	public String getUserEmail(String accessToken) throws BaseException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Bearer " + accessToken);
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(NAVER_USER_INFO_REQUEST_URL, HttpMethod.GET, requestEntity,
			String.class);

		return findEmailByRegex(response.getBody());
	}
}
