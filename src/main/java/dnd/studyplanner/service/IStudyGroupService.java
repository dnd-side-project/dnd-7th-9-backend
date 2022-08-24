package dnd.studyplanner.service;

import java.util.List;

import dnd.studyplanner.dto.studyGroup.response.MyStudyGroupPageResponse;
import dnd.studyplanner.dto.studyGroup.response.MyStudyGroupResponse;
import dnd.studyplanner.domain.studygroup.model.StudyGroupCategory;
import dnd.studyplanner.dto.studyGroup.response.StudyGroupSaveResponse;
import dnd.studyplanner.dto.studyGroup.request.StudyGroupSaveDto;
import dnd.studyplanner.dto.userJoinGroup.request.UserJoinGroupSaveDto;


public interface IStudyGroupService {

	StudyGroupSaveResponse saveGroupAndInvite(StudyGroupSaveDto studyGroupSaveDto, UserJoinGroupSaveDto userJoinGroupSaveDto, String accessToken);

	MyStudyGroupPageResponse getUserStudyGroups(String accessToken, String status);

	List<StudyGroupCategory> getCategoryList(String accessToken);

}
