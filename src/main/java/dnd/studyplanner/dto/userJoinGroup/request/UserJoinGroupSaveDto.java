package dnd.studyplanner.dto.userJoinGroup.request;

import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.domain.user.model.UserJoinGroup;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJoinGroupSaveDto {

	public UserJoinGroup toEntity(User user, StudyGroup studyGroup) {
		return UserJoinGroup.builder()
			.user(user)
			.studyGroup(studyGroup)
			.build();
	}
}
