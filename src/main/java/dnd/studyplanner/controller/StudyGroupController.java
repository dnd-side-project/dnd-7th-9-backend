package dnd.studyplanner.controller;

import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import java.util.List;

import dnd.studyplanner.dto.studyGroup.request.StudyGroupInviteDto;
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
import dnd.studyplanner.dto.studyGroup.response.AgreeInvitedStudyGroupResponse;
import dnd.studyplanner.dto.studyGroup.response.InvitedStudyGroupResponse;
import dnd.studyplanner.dto.studyGroup.response.MyStudyGroupPageResponse;
import dnd.studyplanner.dto.studyGroup.response.StudyGroupSaveResponse;
import dnd.studyplanner.dto.userJoinGroup.request.UserJoinGroupSaveDto;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.jwt.JwtService;
import dnd.studyplanner.service.IStudyGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/group")
@RestController
public class StudyGroupController {

    private final IStudyGroupService studyGroupService;

    private final JwtService jwtService;

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
        @RequestParam(required = false) String status
    ) {
        try {
            if (status == null) {
                status = "complete";
            }

            log.debug("[STUDY GROUP STATUS] : {}", status);
            MyStudyGroupPageResponse response = studyGroupService.getUserStudyGroups(accessToken, status);

            return new CustomResponse<>(response, GET_MY_GROUP_SUCCESS).toResponseEntity();
        } catch (IllegalArgumentException e) {
            return new CustomResponse<>(NOT_VALID_STATUS).toResponseEntity();
        }
    }

    // 스터디 그룹 생성
    @PostMapping("/save")
    public ResponseEntity<CustomResponse> saveStudyGroupOnly (
            @RequestHeader(value = "Access-Token") String accessToken,
            @RequestBody StudyGroupSaveDto studyGroupSaveDto, UserJoinGroupSaveDto userJoinGroupSaveDto) {

        StudyGroupSaveResponse updateStudyGroup = studyGroupService.saveStudyGroupOnly(studyGroupSaveDto, userJoinGroupSaveDto, accessToken);

        return new CustomResponse<>(updateStudyGroup, SAVE_GROUP_SUCCESS).toResponseEntity();

    }

    // 스터디 그룹 초대
    @PostMapping("/invite")
    public ResponseEntity<CustomResponse> groupInvite (
            @RequestHeader(value = "Access-Token") String accessToken,
            @RequestBody StudyGroupInviteDto studyGroupInviteDto, UserJoinGroupSaveDto userJoinGroupSaveDto) {

        StudyGroupSaveResponse updateStudyGroup = studyGroupService.groupInvite(studyGroupInviteDto, userJoinGroupSaveDto, accessToken);

        return new CustomResponse<>(updateStudyGroup, INVITE_USER_SUCCESS).toResponseEntity();

    }

    // 초대 링크를 통한 초대 수락 페이지 접속
    @GetMapping("/invite")
    public ResponseEntity<CustomResponse> getInvitedStudyGroup(
        @RequestHeader(value = "Access-Token") String accessToken,
        @RequestParam Long groupId) {

        if (accessToken == null) {   // 토근이 존재하지 않는 경우 -> 로그인 페이지로 redirect
            return new CustomResponse<>(TOKEN_NULL).toResponseEntity();
        } else if (jwtService.isNotValid(accessToken)) {
            return new CustomResponse<>(TOKEN_INVALID).toResponseEntity();
        } else if (jwtService.isExpired(accessToken)) {
            return new CustomResponse<>(TOKEN_EXPIRED).toResponseEntity();
        }

        InvitedStudyGroupResponse invitedStudyGroupResponse = studyGroupService.getInvitedStudyGroup(accessToken, groupId);

        return new CustomResponse<>(invitedStudyGroupResponse, SUCCESS).toResponseEntity();

    }

}
