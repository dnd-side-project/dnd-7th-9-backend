package dnd.studyplanner.service;

import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.dto.user.response.StudyGroupListGetResponse;
import dnd.studyplanner.dto.user.request.UserInfoExistDto;
import dnd.studyplanner.dto.user.request.UserInfoSaveDto;

import java.util.List;

public interface IUserService {

    User saveUserInfo(UserInfoSaveDto userInfoSaveDto, String userAccessToken);

    boolean checkExistUser(UserInfoExistDto userInfoExistDto);

    boolean isValidEmail(String userMail);

    List<StudyGroupListGetResponse> getUserStudyGroupList(String accessToken);
}
