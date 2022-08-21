package dnd.studyplanner.service;

import java.util.List;

import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import dnd.studyplanner.dto.studyGroup.response.MyStudyGroupResponse;
import dnd.studyplanner.dto.studyGroup.response.StudyGroupListResponse;

import dnd.studyplanner.domain.studygroup.model.StudyGroupCategory;
import dnd.studyplanner.dto.studyGroup.response.StudyGroupSaveResponse;
import dnd.studyplanner.dto.studyGroup.request.StudyGroupSaveDto;
import dnd.studyplanner.dto.userJoinGroup.request.UserJoinGroupSaveDto;

import java.util.List;

public interface IStudyGroupService {

	StudyGroupSaveResponse saveGroupAndInvite(StudyGroupSaveDto studyGroupSaveDto, UserJoinGroupSaveDto userJoinGroupSaveDto, String accessToken);

	List<MyStudyGroupResponse> getUserStudyGroups(String accessToken, String status);

	List<StudyGroupCategory> getCategoryList(String accessToken);

}
