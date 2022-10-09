package dnd.studyplanner.oauth.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dnd.studyplanner.exception.BaseException;

public abstract class OAuthLoginService {

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
