package dnd.studyplanner.auth.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import dnd.studyplanner.member.model.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Entity
public class AuthEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String jwt;

	private String refreshToken;

	private Long memberId;

	@Builder
	public AuthEntity(String jwt, String refreshToken, Long memberId) {
		this.jwt = jwt;
		this.refreshToken = refreshToken;
		this.memberId = memberId;
	}

	public void refreshUpdate(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}