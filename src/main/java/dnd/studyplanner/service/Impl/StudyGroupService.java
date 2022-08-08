package dnd.studyplanner.service.Impl;

import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.domain.user.model.UserJoinGroup;
import dnd.studyplanner.dto.studyGroup.request.StudyGroupSaveDto;
import dnd.studyplanner.jwt.JwtService;
import dnd.studyplanner.repository.StudyGroupRepository;
// import dnd.studyplanner.repository.UserJoinGroupRepository;
import dnd.studyplanner.repository.UserRepository;
import dnd.studyplanner.service.IStudyGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dnd.studyplanner.domain.studygroup.model.StudyGroupStatus.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StudyGroupService implements IStudyGroupService {

	private final UserRepository userRepository;
	private final StudyGroupRepository studyGroupRepository;
	// private final UserJoinGroupRepository userJoinGroupRepository;
	private final JwtService jwtService;

	@Override
	public StudyGroup saveStudyGroup(StudyGroupSaveDto studyGroupSaveDto, String userAccessToken) {

		Long currentUserId = getCurrentUserId(userAccessToken);
		Optional<User> user = userRepository.findById(currentUserId);

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime groupStartDate = studyGroupSaveDto.getGroupStartDate();
		LocalDateTime groupEndDate = studyGroupSaveDto.getGroupEndDate();
		int compareStatus = groupStartDate.compareTo(today);

		if (groupEndDate.isBefore(today)) {  // COMPLETE 상태
			studyGroupSaveDto.setGroupStatus(COMPLETE);
		} else if (compareStatus > 0) {   // READY 상태
			studyGroupSaveDto.setGroupStatus(READY);
		} else if (compareStatus <= 0) {   // ACTIVE 상태
			studyGroupSaveDto.setGroupStatus(ACTIVE);
		}

		StudyGroup studyGroup = studyGroupSaveDto.toEntity(user.get());
		studyGroupRepository.save(studyGroup);

		return studyGroup;
	}

	private Long getCurrentUserId(String userAccessToken) {

		Long currentUserId = jwtService.getUserId(userAccessToken);
		return currentUserId;
	}
}
