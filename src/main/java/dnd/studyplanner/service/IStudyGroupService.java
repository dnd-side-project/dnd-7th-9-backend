package dnd.studyplanner.service;

import java.util.List;

import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import dnd.studyplanner.dto.studyGroup.response.StudyGroupListResponse;
import dnd.studyplanner.dto.studyGroup.response.StudyGroupSaveResponse;
import dnd.studyplanner.dto.studyGroup.request.StudyGroupSaveDto;
import dnd.studyplanner.dto.userJoinGroup.request.UserJoinGroupSaveDto;

public interface IStudyGroupService {

	StudyGroupSaveResponse saveGroupAndInvite(StudyGroupSaveDto studyGroupSaveDto, UserJoinGroupSaveDto userJoinGroupSaveDto, String accessToken);

	List<StudyGroupListResponse> getUserStudyGroups(String accessToken, String status);

}
