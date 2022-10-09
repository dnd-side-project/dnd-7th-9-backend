package dnd.studyplanner.oauth.service;

import dnd.studyplanner.exception.BaseException;

public class GoogleOAuthLoginService extends OAuthLoginService {
	@Override
	public String requestAccessToken(String code) throws BaseException {
		return null;
	}

	@Override
	public String getUserEmail(String accessToken) throws BaseException {
		return null;
	}
}
