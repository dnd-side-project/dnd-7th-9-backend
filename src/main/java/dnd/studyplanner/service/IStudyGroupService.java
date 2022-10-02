package dnd.studyplanner.service;

import java.util.List;

import dnd.studyplanner.domain.studygroup.model.StudyGroupCategory;
import dnd.studyplanner.dto.studyGroup.request.StudyGroupFinishDto;
import dnd.studyplanner.dto.studyGroup.request.StudyGroupInviteDto;
import dnd.studyplanner.dto.studyGroup.request.StudyGroupSaveDto;
import dnd.studyplanner.dto.studyGroup.response.AgreeInvitedStudyGroupResponse;
import dnd.studyplanner.dto.studyGroup.response.InvitedStudyGroupResponse;
import dnd.studyplanner.dto.studyGroup.response.MyStudyGroupPageResponse;
import dnd.studyplanner.dto.studyGroup.response.StudyGroupSaveResponse;
import dnd.studyplanner.dto.userJoinGroup.request.UserJoinGroupSaveDto;
import dnd.studyplanner.exception.BaseException;

public interface IStudyGroupService {

	StudyGroupSaveResponse saveGroupAndInvite(StudyGroupSaveDto studyGroupSaveDto, UserJoinGroupSaveDto userJoinGroupSaveDto, String accessToken);

	MyStudyGroupPageResponse getUserStudyGroups(String accessToken, String status);

	List<StudyGroupCategory> getCategoryList(String accessToken);

	StudyGroupSaveResponse saveStudyGroupOnly(StudyGroupSaveDto studyGroupSaveDto, UserJoinGroupSaveDto userJoinGroupSaveDto,String accessToken) throws BaseException;

	StudyGroupSaveResponse groupInvite(StudyGroupInviteDto studyGroupInviteDto, UserJoinGroupSaveDto userJoinGroupSaveDto, String accessToken) throws BaseException;

	InvitedStudyGroupResponse getInvitedStudyGroup(String accessToken, Long studyGroupId);

	AgreeInvitedStudyGroupResponse agreeInvitedGroup(String accessToken, Long studyGroupId) throws BaseException;

	void finishStudyGroup(String accessToken, StudyGroupFinishDto studyGroupFinishDto) throws BaseException;
}
