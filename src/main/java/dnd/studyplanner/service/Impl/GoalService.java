package dnd.studyplanner.service.Impl;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.dto.goal.request.GoalSaveDto;
import dnd.studyplanner.dto.goal.response.GoalSaveResponse;
import dnd.studyplanner.dto.response.CustomResponseStatus;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.jwt.JwtService;
import dnd.studyplanner.repository.GoalRepository;
import dnd.studyplanner.repository.StudyGroupRepository;
import dnd.studyplanner.repository.UserRepository;
import dnd.studyplanner.service.IGoalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static dnd.studyplanner.domain.goal.model.GoalStatus.*;
import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GoalService implements IGoalService {

    private final UserRepository userRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final GoalRepository goalRepository;
    private final JwtService jwtService;

    @Override
    public GoalSaveResponse addDetailGoal(String accessToken, GoalSaveDto goalSaveDto) throws BaseException {

        Long currentUserId = getCurrentUserId(accessToken);
        User user = userRepository.findById(currentUserId).get();

        Long myStudyGroupId = goalSaveDto.getStudyGroupId();
        Optional<StudyGroup> studyGroup = studyGroupRepository.findById(myStudyGroupId);

        LocalDate today = LocalDate.now();
        LocalDate goalStartDate = goalSaveDto.getGoalStartDate();
        LocalDate goalEndDate = goalSaveDto.getGoalEndDate();
        LocalDate groupStartDate = studyGroup.get().getGroupStartDate();
        LocalDate groupEndDate = studyGroup.get().getGroupEndDate();

        if (goalStartDate.isBefore(groupStartDate) || goalEndDate.isAfter(groupEndDate)) {
            throw new BaseException(CustomResponseStatus.GOAL_EXCEED_GROUP);
        }
        if (goalStartDate.isAfter(goalEndDate)) {
            throw new BaseException(START_AFTER_END);
        }

        int compareStatus = goalStartDate.compareTo(today);

        if (goalEndDate.isBefore(today)) {  // COMPLETE 상태
            goalSaveDto.setGoalStatus(COMPLETE);
        } else if (compareStatus > 0) {   // READY 상태
            goalSaveDto.setGoalStatus(READY);
        } else if (compareStatus <= 0) {   // ACTIVE 상태
            goalSaveDto.setGoalStatus(ACTIVE);
        }

        Goal updateGoal = goalSaveDto.toEntity(user, studyGroup.get());
        goalRepository.save(updateGoal);

        GoalSaveResponse updateGoalSaveResponse = GoalSaveResponse.builder()
            .goal(updateGoal).build();

        return updateGoalSaveResponse;
    }

    private Long getCurrentUserId(String userAccessToken) {

        Long currentUserId = jwtService.getUserId(userAccessToken);
        return currentUserId;
    }
}
