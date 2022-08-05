package dnd.studyplanner.auth.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


// memberId, jwt, refreshToken 저장
@Getter
@RequiredArgsConstructor
@Entity
public class AuthEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String jwt;

	private String refreshToken;

	private Long userId;

	@Builder
	public AuthEntity(String jwt, String refreshToken, Long userId) {
		this.jwt = jwt;
		this.refreshToken = refreshToken;
		this.userId = userId;
	}
}