package dnd.studyplanner.member.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Role {
	USER("ROLE_USER", "일반 사용자"),
	GUEST("ROLE_GUEST", "게스트");

	private final String key;
	private final String title;

	public static Role of(String key) {
		return Arrays.stream(Role.values())
				.filter(r -> r.getKey().equals(key))
				.findAny()
				.orElse(GUEST);
	}
}
