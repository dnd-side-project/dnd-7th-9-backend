package dnd.studyplanner.domain.user.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import dnd.studyplanner.domain.base.BaseEntity;
import lombok.Getter;

@Getter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	// @Column(nullable = false) 테스트를 위해 임시로 미적용
	private String userEmail;

	// @Column(nullable = false) 테스트를 위해 임시로 미적용
	private String accessToken;

	private String userName;
	private int userAge;
	private String userGender;
	private String userRegion;
	private String userProfileImageUrl;


	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserJoinGroup> userJoinGroups = new ArrayList<>();

	@OneToMany(mappedBy = "solveUser", cascade = CascadeType.ALL)
	private List<UserSolveQuestionBook> userSolveQuestionBooks = new ArrayList<>();
}
