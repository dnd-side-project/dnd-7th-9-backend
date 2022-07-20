package dnd.studyplanner.auth.dto;

import java.util.Map;

import dnd.studyplanner.member.model.Member;
import dnd.studyplanner.member.model.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuthAttributes {

	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;

	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.name = name;
		this.email = email;
	}

	public static OAuthAttributes of(String registrationId, String userNameAttributeName,
									 Map<String, Object> attributes) {

		switch (AuthProvider.valueOf(registrationId.toLowerCase())) {
			case naver:
				return ofNaver(userNameAttributeName, attributes);
			case kakao:
				return ofKakao("id", attributes);
		}
		return ofGoogle("sub", attributes);
	}

	private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {

		return OAuthAttributes.builder()
			.name((String)attributes.get("name"))
			.email((String)attributes.get("email"))
			.attributes(attributes)
			.nameAttributeKey(userNameAttributeName)
			.build();
	}

	private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {

		Map<String, Object> response = (Map<String, Object>) attributes.get("response");

		return OAuthAttributes.builder()
			.name((String)attributes.get("name"))
			.email((String)attributes.get("email"))
			.attributes(attributes)
			.nameAttributeKey(userNameAttributeName)
			.build();
	}

	//Kakao Login User 정보 요청
	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		// kakao는 kakao_account에 유저정보가 있다. (email)
		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
		// kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname, profile_image)
		Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

		return OAuthAttributes.builder()
			.name((String) kakaoProfile.get("nickname"))
			.email((String) kakaoAccount.get("email"))
			.attributes(attributes)
			.nameAttributeKey(userNameAttributeName)
			.build();
	}

	public Member toEntity() {
		return Member.builder()
			.nickname(name)
			.email(email)
			.role(Role.USER)
			.build();
	}

}
