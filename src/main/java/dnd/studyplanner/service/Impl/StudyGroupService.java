package dnd.studyplanner.service.Impl;

import static dnd.studyplanner.domain.studygroup.model.StudyGroupStatus.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.studygroup.model.StudyGroupCategory;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.domain.user.model.UserJoinGroup;
import dnd.studyplanner.dto.studyGroup.request.StudyGroupInviteDto;
import dnd.studyplanner.dto.studyGroup.request.StudyGroupSaveDto;
import dnd.studyplanner.dto.studyGroup.response.MyStudyGroupPageResponse;
import dnd.studyplanner.dto.studyGroup.response.MyStudyGroupResponse;
import dnd.studyplanner.dto.studyGroup.response.StudyGroupSaveResponse;
import dnd.studyplanner.dto.userJoinGroup.request.UserJoinGroupSaveDto;
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
	public StudyGroupSaveResponse saveStudyGroupOnly(StudyGroupSaveDto studyGroupSaveDto, UserJoinGroupSaveDto userJoinGroupSaveDto, String accessToken) {

		Long currentUserId = getCurrentUserId(accessToken);
		User hostUser = userRepository.findById(currentUserId).get();   // host

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

		StudyGroup studyGroup = studyGroupSaveDto.toEntity(hostUser);
		studyGroupRepository.save(studyGroup);   // 스터디 그룹 생성

		// Host User 스터디 그룹 가입
		UserJoinGroup updateHostUser = userJoinGroupSaveDto.toEntity(hostUser, studyGroup);
		userJoinGroupRepository.save(updateHostUser);

		StudyGroupSaveResponse studyGroupSaveResponse = StudyGroupSaveResponse.builder()
				.studyGroup(studyGroup)
				.studyGroupMember(Collections.singletonList(hostUser.getUserEmail()))
				.build();

		return studyGroupSaveResponse;
	}

	// TODO 스터디 그룹 생성과 초대 API 분리 - 사용자 이메일 검색 시 목록 조회
	

	// TODO 스터디 그룹 생성과 초대 API 분리 - 사용자 초대
	@Override
	public StudyGroupSaveResponse groupInvite(StudyGroupInviteDto studyGroupInviteDto, UserJoinGroupSaveDto userJoinGroupSaveDto, String accessToken) {

		Long currentUserId = getCurrentUserId(accessToken);
		User hostUser = userRepository.findById(currentUserId).get();
		StudyGroup inviteStudyGroup = studyGroupRepository.findById(studyGroupInviteDto.getStudyGroupId()).get();

		String inviteUserEmail = studyGroupInviteDto.getUserEmail();
		if (isValidEmail(inviteUserEmail) && checkExistUser(inviteUserEmail)) {
			if (!hostUser.getUserEmail().equals(inviteUserEmail)) {
				User inviteUser = userRepository.findByUserEmail(inviteUserEmail).get();
				UserJoinGroup updateInvitedPeople = userJoinGroupSaveDto.toEntity(inviteUser, inviteStudyGroup);

				userJoinGroupRepository.save(updateInvitedPeople);
			}
		}

		List<String> groupUserList = new ArrayList<>();
		List<UserJoinGroup> userJoinGroupList = inviteStudyGroup.getUserJoinGroups();
		for (UserJoinGroup userJoinGroup : userJoinGroupList) {
			groupUserList.add(userJoinGroup.getUser().getUserEmail());
		}
		// 사용자
		StudyGroupSaveResponse studyGroupSaveResponse = StudyGroupSaveResponse.builder()
				.studyGroup(inviteStudyGroup)
				.studyGroupMember(groupUserList)
				.build();

		return studyGroupSaveResponse;
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
