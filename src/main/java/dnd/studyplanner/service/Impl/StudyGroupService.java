package dnd.studyplanner.service.Impl;

import static dnd.studyplanner.domain.studygroup.model.StudyGroupStatus.*;
import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.goal.model.GoalEndDateComparator;
import dnd.studyplanner.domain.goal.model.GoalStatus;
import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.studygroup.model.StudyGroupCategory;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.domain.user.model.UserJoinGroup;
import dnd.studyplanner.dto.goal.response.ActiveGoalResponse;
import dnd.studyplanner.dto.response.CustomResponse;
import dnd.studyplanner.dto.studyGroup.request.StudyGroupFinishDto;
import dnd.studyplanner.dto.studyGroup.request.StudyGroupInviteDto;
import dnd.studyplanner.dto.studyGroup.request.StudyGroupSaveDto;
import dnd.studyplanner.dto.studyGroup.response.AgreeInvitedStudyGroupResponse;
import dnd.studyplanner.dto.studyGroup.response.InvitedStudyGroupResponse;
import dnd.studyplanner.dto.studyGroup.response.MyStudyGroupPageResponse;
import dnd.studyplanner.dto.studyGroup.response.MyStudyGroupResponse;
import dnd.studyplanner.dto.studyGroup.response.StudyGroupSaveResponse;
import dnd.studyplanner.dto.userJoinGroup.request.UserJoinGroupSaveDto;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.jwt.JwtService;
import dnd.studyplanner.repository.StudyGroupRepository;
import dnd.studyplanner.repository.UserJoinGroupRepository;
import dnd.studyplanner.repository.UserRepository;
import dnd.studyplanner.service.IStudyGroupService;
import dnd.studyplanner.service.IUserRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StudyGroupService implements IStudyGroupService {

	private final UserRepository userRepository;
	private final StudyGroupRepository studyGroupRepository;
	private final UserJoinGroupRepository userJoinGroupRepository;
	private final JwtService jwtService;
	private final IUserRateService userRateService;

	private final UserService userService;

	@Override
	public StudyGroupSaveResponse saveGroupAndInvite(StudyGroupSaveDto studyGroupSaveDto, UserJoinGroupSaveDto userJoinGroupSaveDto, String accessToken) {

		StudyGroup updateStudyGroup = saveStudyGroup(studyGroupSaveDto, accessToken);
		Long updateGroupId = updateStudyGroup.getId();

		List<String> updateStudyGroupMemberList = checkInvitedPeople(studyGroupSaveDto);
		List<UserJoinGroup> invitedPeopleList = new ArrayList<>();

		StudyGroup joinStudyGroup = studyGroupRepository.findById(updateGroupId).get();

		Long currentUserId = getCurrentUserId(accessToken);
		User hostUser = userRepository.findById(currentUserId).get();
		UserJoinGroup updateHostPeople = userJoinGroupSaveDto.toEntity(hostUser, joinStudyGroup);
		invitedPeopleList.add(updateHostPeople);
		updateStudyGroupMemberList.add(hostUser.getUserEmail());

		for (String invitedPeople : updateStudyGroupMemberList) {
			User invitedUser = userRepository.findByUserEmail(invitedPeople).get();
			if (hostUser.getUserEmail().equals(invitedUser.getUserEmail())) continue;
			UserJoinGroup updateInvitedPeople = userJoinGroupSaveDto.toEntity(invitedUser, joinStudyGroup);
			invitedPeopleList.add(updateInvitedPeople);
		}

		userJoinGroupRepository.saveAll(invitedPeopleList);

		StudyGroupSaveResponse studyGroupSaveResponse = StudyGroupSaveResponse.builder()
			.studyGroup(updateStudyGroup)
			.studyGroupMember(updateStudyGroupMemberList)
			.build();

		return studyGroupSaveResponse;
	}

	@Override
	public MyStudyGroupPageResponse getUserStudyGroups(String accessToken, String status) {
		Long currentUserId = getCurrentUserId(accessToken);
		StudyGroupStatus studyGroupStatus = StudyGroupStatus.valueOf(status.toUpperCase());


		List<MyStudyGroupResponse> studyGroupList = userJoinGroupRepository.findByUserId(currentUserId).stream()
				.map(UserJoinGroup::getStudyGroup)
				.filter(studyGroup -> studyGroup.getGroupStatus().equals(studyGroupStatus))
				.map(userGroup -> MyStudyGroupResponse.builder()
						.groupId(userGroup.getId())
						.groupName(userGroup.getGroupName())
						.groupStartDate(userGroup.getGroupStartDate())
						.groupEndDate(userGroup.getGroupEndDate())
						.groupGoal(userGroup.getGroupGoal())
						.groupImageUrl(userGroup.getGroupImageUrl())
						.groupCategory(userGroup.getGroupCategory())
						.groupStatus(userGroup.getGroupStatus())
						.studyGroupRate(userRateService.getUserStudyGroupRate(accessToken, userGroup.getId()))
						.build())
				.collect(Collectors.toList());

		User user = userRepository.findById(currentUserId).get();
		String profileImageUrl = user.getUserProfileImageUrl();
		String userNickName = user.getUserNickName();

		return MyStudyGroupPageResponse.builder()
			.profileImageUrl(profileImageUrl)
			.nickname(userNickName)
			.studyGroupResponses(studyGroupList)
			.build();
	}

	public List<StudyGroupCategory> getCategoryList(String accessToken) {

		List<StudyGroupCategory> categoryList = new ArrayList<>();
		categoryList.addAll(Arrays.asList(StudyGroupCategory.values()));
		return categoryList;
	}

	// TODO 스터디 그룹 생성과 초대 API 분리 - 스터디 그룹 생성
	@Override
	public StudyGroupSaveResponse saveStudyGroupOnly(StudyGroupSaveDto studyGroupSaveDto, UserJoinGroupSaveDto userJoinGroupSaveDto, String accessToken) throws BaseException {

		Long currentUserId = getCurrentUserId(accessToken);
		User hostUser = userRepository.findById(currentUserId).get();   // host

		LocalDate today = LocalDate.now();
		LocalDate groupStartDate = studyGroupSaveDto.getGroupStartDate();
		LocalDate groupEndDate = studyGroupSaveDto.getGroupEndDate();
		// 입력받은 시작 날짜 < 종료 날짜 체크
		if (groupStartDate.isAfter(groupEndDate)) {
			throw new BaseException(START_AFTER_END);
		}
		
		int compareStatus = groupStartDate.compareTo(today);

		if (groupEndDate.isBefore(today)) {  // COMPLETE 상태
			studyGroupSaveDto.setGroupStatus(COMPLETE);
		} else if (compareStatus > 0) {   // READY 상태
			studyGroupSaveDto.setGroupStatus(READY);
		} else if (compareStatus <= 0) {   // ACTIVE 상태
			studyGroupSaveDto.setGroupStatus(ACTIVE);
		}

		StudyGroup studyGroup = studyGroupSaveDto.toEntity(hostUser);
		studyGroupRepository.save(studyGroup);   // 스터디 그룹 생성

		// Host User 스터디 그룹 가입
		UserJoinGroup updateHostUser = userJoinGroupSaveDto.toEntity(hostUser, studyGroup);
		userJoinGroupRepository.save(updateHostUser);

		String newGroupId = String.valueOf(studyGroup.getId());   // 바로 직전에 생성한 그룹의 ID
		String invitedStudyGroupUrl = "https://run-us.netlify.app/invited-group/" + newGroupId;

		StudyGroupSaveResponse studyGroupSaveResponse = StudyGroupSaveResponse.builder()
				.studyGroup(studyGroup)
				.studyGroupMember(Collections.singletonList(hostUser.getUserEmail()))
				.invitedUrl(invitedStudyGroupUrl)
				.build();

		return studyGroupSaveResponse;
	}

	// TODO 스터디 그룹 생성과 초대 API 분리 - 사용자 이메일 검색 시 목록 조회
	

	// TODO 스터디 그룹 생성과 초대 API 분리 - 사용자 초대
	@Override
	public StudyGroupSaveResponse groupInvite(StudyGroupInviteDto studyGroupInviteDto, UserJoinGroupSaveDto userJoinGroupSaveDto, String accessToken) throws BaseException {

		Long currentUserId = getCurrentUserId(accessToken);
		User hostUser = userRepository.findById(currentUserId).get();
		StudyGroup inviteStudyGroup = studyGroupRepository.findById(studyGroupInviteDto.getStudyGroupId()).get();

		// 현재 스터디 그룹의 사용자 정보
		List<UserJoinGroup> userJoinGroupList = inviteStudyGroup.getUserJoinGroups();
		List<String> userEmailList = new ArrayList<>();
		for (UserJoinGroup userJoinGroup : userJoinGroupList) {
			userEmailList.add(userJoinGroup.getUser().getUserEmail());
		}

		String inviteUserEmail = studyGroupInviteDto.getUserEmail();
		if (isValidEmail(inviteUserEmail) && checkExistUser(inviteUserEmail)) {
			if (!hostUser.getUserEmail().equals(inviteUserEmail)) {
				// 이미 가입된 사용자가 아닌 경우에만 가입시키도록
				if (userEmailList.contains(inviteUserEmail)) {
					throw new BaseException(USER_ALREADY_IN_GROUP);
				}
				User inviteUser = userRepository.findByUserEmail(inviteUserEmail).get();
				UserJoinGroup updateInvitedPeople = userJoinGroupSaveDto.toEntity(inviteUser, inviteStudyGroup);

				userJoinGroupRepository.save(updateInvitedPeople);
			}
		}

		List<String> groupUserList = new ArrayList<>();
		userJoinGroupList = inviteStudyGroup.getUserJoinGroups();
		for (UserJoinGroup userJoinGroup : userJoinGroupList) {
			// 방장 이메일 제외
			String userEmail = userJoinGroup.getUser().getUserEmail();
			if (userEmail.equals(userJoinGroup.getStudyGroup().getGroupCreateUser().getUserEmail())) {
				continue;
			}
			groupUserList.add(userEmail);
		}

		String newGroupId = String.valueOf(studyGroupInviteDto.getStudyGroupId());   // 바로 직전에 생성한 그룹의 ID
		String invitedStudyGroupUrl = "https://run-us.netlify.app/invited-group/" + newGroupId;
		// TODO 초대 링크(초대 수락 페이지) 함께 리턴
		StudyGroupSaveResponse studyGroupSaveResponse = StudyGroupSaveResponse.builder()
				.studyGroup(inviteStudyGroup)
				.studyGroupMember(groupUserList)
				.invitedUrl(invitedStudyGroupUrl)
				.build();

		return studyGroupSaveResponse;
	}

	// 초대 링크를 통한 초대 수락 페이지 접속
	@Override
	public InvitedStudyGroupResponse getInvitedStudyGroup(String accessToken, Long studyGroupId) {

		Long currentUserId = getCurrentUserId(accessToken);
		User user = userRepository.findById(currentUserId).get();

		StudyGroup invitedStudyGroup = studyGroupRepository.findById(studyGroupId).get();

		InvitedStudyGroupResponse invitedStudyGroupResponse = InvitedStudyGroupResponse.builder()
			.studyGroup(invitedStudyGroup)
			.build();

		return invitedStudyGroupResponse;
	}

	// 초대 링크 접속 후 초대 수락 클릭 -> 해당 사용자 가입 처리
	@Override
	public AgreeInvitedStudyGroupResponse agreeInvitedGroup(String accessToken, Long studyGroupId) throws BaseException {

		Long currentUserId = getCurrentUserId(accessToken);
		User user = userRepository.findById(currentUserId).get();
		StudyGroup invitedStudyGroup = studyGroupRepository.findById(studyGroupId).get();

		// TODO 이미 스터디 그룹에 가입되어 있는 경우
		if (existInStudyGroup(user, invitedStudyGroup)) {
			throw new BaseException(USER_ALREADY_IN_GROUP);
		}
		// TODO 가입
		UserJoinGroup updateUser = UserJoinGroup.builder().user(user).studyGroup(invitedStudyGroup).build();
		userJoinGroupRepository.save(updateUser);

		AgreeInvitedStudyGroupResponse agreeInvitedStudyGroupResponse = AgreeInvitedStudyGroupResponse.builder()
			.studyGroup(invitedStudyGroup)
			.build();

		return agreeInvitedStudyGroupResponse;

	}

	@Override
	public void finishStudyGroup(String accessToken, StudyGroupFinishDto studyGroupFinishDto) throws BaseException {
		Long userId = getCurrentUserId(accessToken);
		StudyGroup studyGroup = studyGroupRepository.findById(studyGroupFinishDto.getStudyGroupId()).orElseThrow(
				() -> new BaseException(NOT_EXIST_DATA)
			);

		if (!studyGroup.getGroupCreateUser().getId().equals(userId)) {
			throw new BaseException(UNAUTHORIZED_REQUEST);
		}

		studyGroup.updateStatus(COMPLETE);

		// 스터디 그룹 내 세부 목표들 완료 처리
		List<Goal> groupGoalList = studyGroup.getGroupDetailGoals();
		for (Goal goal : groupGoalList) {
			goal.updateStatus(GoalStatus.COMPLETE);
		}
	}

	private boolean existInStudyGroup(User user, StudyGroup studyGroup) {

		boolean check = false;
		List<UserJoinGroup> userJoinGroupList = user.getUserJoinGroups();
		for (UserJoinGroup userJoinGroup : userJoinGroupList) {
			if (userJoinGroup.getStudyGroup().getId().equals(studyGroup.getId())) {
				check = true;
			}
		}
		return check;
	}

	private StudyGroup saveStudyGroup(StudyGroupSaveDto studyGroupSaveDto, String userAccessToken) {

		Long currentUserId = getCurrentUserId(userAccessToken);
		User user = userRepository.findById(currentUserId).get();

		LocalDate today = LocalDate.now();
		LocalDate groupStartDate = studyGroupSaveDto.getGroupStartDate();
		LocalDate groupEndDate = studyGroupSaveDto.getGroupEndDate();
		int compareStatus = groupStartDate.compareTo(today);

		if (groupEndDate.isBefore(today)) {  // COMPLETE 상태
			studyGroupSaveDto.setGroupStatus(COMPLETE);
		} else if (compareStatus > 0) {   // READY 상태
			studyGroupSaveDto.setGroupStatus(READY);
		} else if (compareStatus <= 0) {   // ACTIVE 상태
			studyGroupSaveDto.setGroupStatus(ACTIVE);
		}

		StudyGroup studyGroup = studyGroupSaveDto.toEntity(user);
		studyGroupRepository.save(studyGroup);

		return studyGroup;
	}

	private Long getCurrentUserId(String userAccessToken) {

		Long currentUserId = jwtService.getUserId(userAccessToken);
		return currentUserId;
	}

	private List<String> checkInvitedPeople(StudyGroupSaveDto studyGroupSaveDto) {

		List<String> correctInvitedUserEmailList = new ArrayList<>();

		List<String> invitedUserEmailList = studyGroupSaveDto.getInvitedUserEmailList();
		for (String invitedUserEmail : invitedUserEmailList) {
			if (!isValidEmail(invitedUserEmail) || !checkExistUser(invitedUserEmail)) {
				continue;
			}
			correctInvitedUserEmailList.add(invitedUserEmail);
		}
		return correctInvitedUserEmailList;
	}

	private boolean checkExistUser(String invitedUserEmail) {

		try {
			User findUser = userRepository.findByUserEmail(invitedUserEmail).get();
			return true;
		} catch (NoSuchElementException exception) {
			return false;
		}
	}

	private boolean isValidEmail(String invitedUserEmail) {

		boolean check = false;
		String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(invitedUserEmail);
		if(m.matches()) {
			check = true;
		}
		return check;
	}

}
