package dnd.studyplanner.controller;

import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.user.model.UserJoinGroup;
import dnd.studyplanner.dto.response.CustomResponse;
import dnd.studyplanner.dto.response.StudyGroupSaveResponse;
import dnd.studyplanner.dto.studyGroup.request.StudyGroupSaveDto;
import dnd.studyplanner.dto.userJoinGroup.request.UserJoinGroupSaveDto;
import dnd.studyplanner.service.IStudyGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dnd.studyplanner.dto.response.CustomResponseStatus.SAVE_GROUP_SUCCESS;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/group")
@RestController
public class StudyGroupController {

    private final IStudyGroupService studyGroupService;

    // 그룹 생성 & 사용자 초대
    @PostMapping("/info")
    public ResponseEntity<CustomResponse> addStudyGroup(
            @RequestHeader(value = "Access-Token") String accessToken,
            @RequestBody StudyGroupSaveDto studyGroupSaveDto, UserJoinGroupSaveDto userJoinGroupSaveDto) {

        StudyGroupSaveResponse updateStudyGroup = studyGroupService.saveGroupAndInvite(studyGroupSaveDto, userJoinGroupSaveDto, accessToken);

        return new CustomResponse<>(updateStudyGroup, SAVE_GROUP_SUCCESS).toResponseEntity();

    }

}
