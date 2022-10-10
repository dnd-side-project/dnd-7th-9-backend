package dnd.studyplanner.oauth.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import dnd.studyplanner.auth.model.AuthRepository;
import dnd.studyplanner.exception.BaseException;
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

	abstract public String requestAccessToken(String code) throws BaseException;


	abstract public String getUserEmail(String accessToken) throws BaseException;


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
}
