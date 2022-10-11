package dnd.studyplanner.oauth.service;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import dnd.studyplanner.auth.model.AuthEntity;
import dnd.studyplanner.auth.model.AuthRepository;
import dnd.studyplanner.config.dto.OAuthAttributes;
import dnd.studyplanner.domain.user.model.Role;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.jwt.JwtService;
import dnd.studyplanner.oauth.dto.response.LoginSuccessResponseDto;
import dnd.studyplanner.oauth.model.OAuthProperties;
import dnd.studyplanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public abstract class OAuthLoginService {

	protected final ObjectMapper objectMapper;
	protected final OAuthProperties oAuthProperties;
	protected final UserRepository userRepository;
	protected final AuthRepository authRepository;

	protected final JwtService jwtService;

	abstract public String requestAccessToken(String code) throws BaseException;


	abstract public String getUserEmail(String accessToken) throws BaseException;

	public LoginSuccessResponseDto logInByEmail(String email) {
		User user = saveNewUser(email);
		saveOrUpdateAuthEntity(user);

		AuthEntity authEntity = authRepository.findById(user.getId()).get();
		String accessToken = authEntity.getJwt();
		String refreshToken = authEntity.getRefreshToken();

		LoginSuccessResponseDto response = LoginSuccessResponseDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.email(user.getUserEmail())
			.isNewUser(user.isNewUser())
			.build();

		if (user.isNewUser()) {
			user.updateNewUser();
			userRepository.save(user);
		}

		return response;
	}


	protected String findEmailByRegex(String attributes) {
		Pattern p = Pattern.compile("([\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Za-z]{2,4})");
		Matcher m = p.matcher(attributes);

		while (m.find()) {
			if (m.group(1) != null) {
				break;
			}
		}

		return m.group(1);
	}

	private User saveNewUser(String email) {
		User user = userRepository.findByUserEmail(email)
			.orElse(User.builder()
				.userEmail(email)
				.role(Role.USER)
				.build());

		userRepository.save(user);
		if (user.getUserNickName() == null) {
			user.saveUserNameAsDefaultValue();
		}

		return user;
	}

	private void saveOrUpdateAuthEntity(User user) {
		AuthEntity authEntity = authRepository.findById(user.getId()).orElse(
			AuthEntity.builder()
				.userId(user.getId())
				.build()
		);

		String jwt = jwtService.createJwt(user.getId());
		String refreshToken = jwtService.createRefreshToken(user.getId());
		authEntity.updateTokens(jwt, refreshToken);
		authRepository.save(authEntity);
	}
}
