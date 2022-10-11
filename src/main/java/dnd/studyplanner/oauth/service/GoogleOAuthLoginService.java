package dnd.studyplanner.oauth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import dnd.studyplanner.auth.model.AuthRepository;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.jwt.JwtService;
import dnd.studyplanner.oauth.model.OAuthProperties;
import dnd.studyplanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
@Service
public class GoogleOAuthLoginService extends OAuthLoginService {
	public GoogleOAuthLoginService(
		ObjectMapper objectMapper,
		OAuthProperties oAuthProperties,
		UserRepository userRepository,
		AuthRepository authRepository,
		JwtService jwtService
	) {
		super(objectMapper, oAuthProperties, userRepository, authRepository, jwtService);
	}

	@Override
	public String requestAccessToken(String code) throws BaseException {
		return null;
	}

	@Override
	public String getUserEmail(String accessToken) throws BaseException {
		return null;
	}
}
