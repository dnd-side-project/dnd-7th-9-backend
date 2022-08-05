package dnd.studyplanner.domain.user.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import dnd.studyplanner.domain.base.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(nullable = false) // 테스트를 위해 임시로 미적용
	private String userEmail;

	// @Column(nullable = false) // 테스트를 위해 임시로 미적용
	private String accessToken;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	private String userNickName;
	private String userName;
	private int userAge;
	private String userGender;
	private String userRegion;
	private String userProfileImageUrl;


	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserJoinGroup> userJoinGroups = new ArrayList<>();

	@OneToMany(mappedBy = "solveUser", cascade = CascadeType.ALL)
	private List<UserSolveQuestionBook> userSolveQuestionBooks = new ArrayList<>();

	@Builder
	public User(String userEmail, String accessToken, Role role, String userNickName, String userName, int userAge
			, String userGender, String userRegion, String userProfileImageUrl) {
		this.userEmail = userEmail;
		this.accessToken = accessToken;
		this.role = role;
		this.userNickName = userNickName;
		this.userName = userName;
		this.userAge = userAge;
		this.userGender = userGender;
		this.userRegion = userRegion;
		this.userProfileImageUrl = userProfileImageUrl;
	}

	public String getRoleKey() {
		return this.role.getKey();
	}

	public void update(String userName, int userAge, String userGender, String userRegion, String userProfileImageUrl) {
		this.userName = userName;
		this.userAge = userAge;
		this.userGender = userGender;
		this.userRegion = userRegion;
		this.userProfileImageUrl = userProfileImageUrl;
	}

}
