package dnd.studyplanner.service;

import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.dto.user.response.UserEmailListResponse;
import dnd.studyplanner.dto.user.response.UserStudyGroupListDetailResponse;
import dnd.studyplanner.dto.user.response.groupList.StudyGroupListGetResponse;
import dnd.studyplanner.dto.user.request.UserInfoExistDto;
import dnd.studyplanner.dto.user.request.UserInfoSaveDto;
import dnd.studyplanner.dto.user.response.usergoal.UserGoalListResponse;
import dnd.studyplanner.exception.BaseException;

import java.util.List;

public interface IUserService {

    User saveUserInfo(UserInfoSaveDto userInfoSaveDto, String userAccessToken);

    boolean checkExistUser(UserInfoExistDto userInfoExistDto);

    boolean isValidEmail(String userMail);

    List<StudyGroupListGetResponse> getUserStudyGroupList(String accessToken);

    UserGoalListResponse getUserStudyGroupListV2(String accessToken) throws BaseException;

    UserStudyGroupListDetailResponse getUserStudyGroupListDetail(String accessToken, Long groupId, Long goalId, String version) throws BaseException;

    UserEmailListResponse getUserEmailList(String userEmail);
}
