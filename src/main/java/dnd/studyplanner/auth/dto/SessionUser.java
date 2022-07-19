package dnd.studyplanner.auth.dto;

import java.io.Serializable;

import dnd.studyplanner.member.model.Member;
import lombok.Getter;

@Getter
public class SessionUser implements Serializable {
	private String nickname;
	private String email;

	public SessionUser(Member member) {
		this.nickname = member.getNickname();
		this.email = member.getEmail();
	}
}
