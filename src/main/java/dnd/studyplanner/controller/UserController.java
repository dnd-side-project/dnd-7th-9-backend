package dnd.studyplanner.controller;

import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.dto.response.CustomResponse;
import dnd.studyplanner.dto.studyGroup.response.StudyGroupListResponse;
import dnd.studyplanner.dto.user.request.UserInfoExistDto;
import dnd.studyplanner.dto.user.request.UserInfoSaveDto;
import dnd.studyplanner.service.IStudyGroupService;
import dnd.studyplanner.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final IUserService userService;
    private final IStudyGroupService studyGroupService;

    @PostMapping("/info")
    public ResponseEntity<CustomResponse> addUserInfo (
            @RequestHeader(value = "Access-Token") String accessToken,
            @RequestBody UserInfoSaveDto userInfoSaveDto) {

        log.debug("[RequestHeader] : {}", accessToken);
        User updateUser = userService.saveUserInfo(userInfoSaveDto, accessToken);

        return new CustomResponse<>(updateUser, SAVE_USER_SUCCESS).toResponseEntity();
    }

    // 사용자 초대 버튼 클릭 시 이메일 존재 여부 확인
    @PostMapping("/exist")
    public ResponseEntity<CustomResponse> existUser (
            @RequestBody UserInfoExistDto userInfoExistDto) {

        // 이메일 유효성 체크
        if (!userService.isValidEmail(userInfoExistDto.getUserEmail())) {
            return new CustomResponse<>(false, NOT_VALID_USER).toResponseEntity();
        }
        // 존재 여부
        if (!userService.checkExistUser(userInfoExistDto)) {
            return new CustomResponse<>(false, NOT_EXIST_USER).toResponseEntity();
        }
        return new CustomResponse<>(userInfoExistDto.getUserEmail(), SUCCESS).toResponseEntity();
    }

    @GetMapping("/list")
    public ResponseEntity<CustomResponse> getStudyGroupList(
            @RequestHeader(value = "Access-Token") String accessToken) {

        List<StudyGroupListResponse> groupList = userService.getStudyGroupList(accessToken);
        return new CustomResponse<>(groupList, GET_GROUP_SUCCESS).toResponseEntity();
    }

}
