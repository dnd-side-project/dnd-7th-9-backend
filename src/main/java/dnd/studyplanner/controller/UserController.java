package dnd.studyplanner.controller;

import com.amazonaws.Response;

import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.dto.response.CustomResponse;
import dnd.studyplanner.dto.user.request.UserIdDto;
import dnd.studyplanner.dto.user.response.UserEmailListResponse;
import dnd.studyplanner.dto.user.response.UserStudyGroupListDetailResponse;
import dnd.studyplanner.dto.user.response.groupList.StudyGroupListGetResponse;
import dnd.studyplanner.dto.user.request.UserInfoExistDto;
import dnd.studyplanner.dto.user.request.UserInfoSaveDto;
import dnd.studyplanner.dto.user.response.usergoal.UserGoalListResponse;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.service.IStudyGroupService;
import dnd.studyplanner.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

	private final IUserService userService;
	private final IStudyGroupService studyGroupService;

	@PostMapping("/info")
	public ResponseEntity<CustomResponse> addUserInfo(
		@RequestHeader(value = "Access-Token") String accessToken,
		@RequestBody UserInfoSaveDto userInfoSaveDto) {

		log.debug("[RequestHeader] : {}", accessToken);
		User updateUser = userService.saveUserInfo(userInfoSaveDto, accessToken);

		return new CustomResponse<>(updateUser, SAVE_USER_SUCCESS).toResponseEntity();
	}

	// 사용자 초대 버튼 클릭 시 이메일 존재 여부 확인
	@PostMapping("/exist")
	public ResponseEntity<CustomResponse> existUser(
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
	public ResponseEntity<CustomResponse> getUserStudyGroupList(
		@RequestHeader(value = "Access-Token") String accessToken) {

		List<StudyGroupListGetResponse> userGroupList = userService.getUserStudyGroupList(accessToken);
		return new CustomResponse<>(userGroupList, GET_GROUP_SUCCESS).toResponseEntity();
	}

	@GetMapping("/list/v2")
	public ResponseEntity<CustomResponse> getUserStudyGroupListV2(
		@RequestHeader(value = "Access-Token") String accessToken) {
		try {
			UserGoalListResponse userGroupList = userService.getUserStudyGroupListV2(accessToken);
			return new CustomResponse<>(userGroupList, GET_GROUP_SUCCESS).toResponseEntity();
		} catch (BaseException e) {
			return new CustomResponse<>(USER_NOT_IN_GROUP).toResponseEntity();
		}
	}

	@GetMapping("/list/detail")
	public ResponseEntity<CustomResponse> getUserStudyGroupListDetail(
		@RequestHeader(value = "Access-Token") String accessToken,
		@RequestParam Long groupId,
		@RequestParam(required = false) Long goalId,
		@RequestParam(required = false) String version) {

		try {
			UserStudyGroupListDetailResponse userStudyGroupListDetailResponse = userService.getUserStudyGroupListDetail(
				accessToken, groupId, goalId, version);
			return new CustomResponse<>(userStudyGroupListDetailResponse, GET_GROUP_DETAIL_SUCCESS).toResponseEntity();
		} catch (BaseException e) {
			return new CustomResponse<>(USER_NOT_IN_GROUP).toResponseEntity();
		}
	}

	// 사용자 이메일 검색 목록 조회
	@GetMapping("/exist")
	public ResponseEntity<CustomResponse> getUserEmailList(@RequestParam String userEmail) {

		//        UserEmailListResponse userEmailListResponse = userService.getUserEmailList(userEmail);
		return new CustomResponse<>(userService.getUserEmailList(userEmail), SUCCESS).toResponseEntity();
	}
}