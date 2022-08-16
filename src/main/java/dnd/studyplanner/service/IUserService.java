package dnd.studyplanner.service;

import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.dto.studyGroup.response.StudyGroupListResponse;
import dnd.studyplanner.dto.user.request.UserInfoExistDto;
import dnd.studyplanner.dto.user.request.UserInfoSaveDto;

import java.util.List;

public interface IUserService {

    User saveUserInfo(UserInfoSaveDto userInfoSaveDto, String userAccessToken);

    boolean checkExistUser(UserInfoExistDto userInfoExistDto);

    boolean isValidEmail(String userMail);

    List<StudyGroupListResponse> getStudyGroupList(String accessToken);
}
