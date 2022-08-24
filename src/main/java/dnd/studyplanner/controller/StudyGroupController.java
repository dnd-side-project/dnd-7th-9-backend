package dnd.studyplanner.controller;

import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dnd.studyplanner.domain.studygroup.model.StudyGroupCategory;
import dnd.studyplanner.dto.response.CustomResponse;
import dnd.studyplanner.dto.studyGroup.request.StudyGroupSaveDto;
import dnd.studyplanner.dto.studyGroup.response.MyStudyGroupPageResponse;
import dnd.studyplanner.dto.studyGroup.response.MyStudyGroupResponse;
import dnd.studyplanner.dto.studyGroup.response.StudyGroupSaveResponse;
import dnd.studyplanner.dto.userJoinGroup.request.UserJoinGroupSaveDto;
import dnd.studyplanner.service.IStudyGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/group")
@RestController
public class StudyGroupController {

    private final IStudyGroupService studyGroupService;

    // 스터디 그룹 생성 시 카테고리 리스트 조회
    @GetMapping("/category")
    public ResponseEntity<CustomResponse> getStudyGroupCategory(
            @RequestHeader(value = "Access-Token") String accessToken
    ) {
        List<StudyGroupCategory> studyGroupCategoryList = studyGroupService.getCategoryList(accessToken);

        return new CustomResponse<>(studyGroupCategoryList, SUCCESS).toResponseEntity();
    }


    // 그룹 생성 & 사용자 초대
    @PostMapping("/info")
    public ResponseEntity<CustomResponse> addStudyGroup(
            @RequestHeader(value = "Access-Token") String accessToken,
            @RequestBody StudyGroupSaveDto studyGroupSaveDto, UserJoinGroupSaveDto userJoinGroupSaveDto) {

        StudyGroupSaveResponse updateStudyGroup = studyGroupService.saveGroupAndInvite(studyGroupSaveDto, userJoinGroupSaveDto, accessToken);

        return new CustomResponse<>(updateStudyGroup, SAVE_GROUP_SUCCESS).toResponseEntity();

    }

    @GetMapping("/my")
    public ResponseEntity<CustomResponse> getMyStudyGroups(
        @RequestHeader(value = "Access-Token") String accessToken,
        @RequestParam String status
    ) {

        if (status == null) {
            return new CustomResponse<>(REQUEST_DATA_NULL).toResponseEntity();
        }

        try {
            log.debug("[STUDY GROUP STATUS] : {}", status);
            MyStudyGroupPageResponse response = studyGroupService.getUserStudyGroups(accessToken, status);

            return new CustomResponse<>(response, GET_MY_GROUP_SUCCESS).toResponseEntity();
        } catch (IllegalArgumentException e) {
            return new CustomResponse<>(NOT_VALID_STATUS).toResponseEntity();
        }
    }

}
