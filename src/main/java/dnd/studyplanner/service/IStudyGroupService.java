package dnd.studyplanner.service;

import dnd.studyplanner.domain.studygroup.model.StudyGroupCategory;
import dnd.studyplanner.dto.studyGroup.response.StudyGroupSaveResponse;
import dnd.studyplanner.dto.studyGroup.request.StudyGroupSaveDto;
import dnd.studyplanner.dto.userJoinGroup.request.UserJoinGroupSaveDto;

import java.util.List;

public interface IStudyGroupService {

	StudyGroupSaveResponse saveGroupAndInvite(StudyGroupSaveDto studyGroupSaveDto, UserJoinGroupSaveDto userJoinGroupSaveDto, String accessToken);

	List<StudyGroupCategory> getCategoryList(String accessToken);
}
