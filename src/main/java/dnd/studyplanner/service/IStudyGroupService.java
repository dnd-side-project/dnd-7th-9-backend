package dnd.studyplanner.service;

import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.dto.studyGroup.request.StudyGroupSaveDto;

public interface IStudyGroupService {

	StudyGroup saveStudyGroup(StudyGroupSaveDto studyGroupSaveDto, String accessToken);

}
