package dnd.studyplanner.service.Impl;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.goal.model.GoalStatus;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import dnd.studyplanner.domain.user.model.*;
import dnd.studyplanner.dto.goal.response.ActiveGoalResponse;
import dnd.studyplanner.dto.user.response.StudyGroupListGetResponse;
import dnd.studyplanner.dto.user.request.UserInfoExistDto;
import dnd.studyplanner.dto.user.request.UserInfoSaveDto;
import dnd.studyplanner.dto.user.response.StudyGroupListResponse;
import dnd.studyplanner.jwt.JwtService;
import dnd.studyplanner.repository.*;
import dnd.studyplanner.service.IQuestionBookService;
import dnd.studyplanner.service.IUserRateService;
import dnd.studyplanner.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final UserSolveQuestionBookRepository userSolveQuestionBookRepository;
    private final UserGoalRateRepository userGoalRateRepository;

    private final IUserRateService userRateService;
    private final JwtService jwtService;
    private final IQuestionBookService questionBookService;

    public User saveUserInfo(UserInfoSaveDto userInfoSaveDto, String userAccessToken) {

        Long currentUserId = getCurrentUserId(userAccessToken);

        Optional<User> user = userRepository.findById(currentUserId);
        user.ifPresent(
            selectUser -> {
                selectUser.update(
                    userInfoSaveDto.getUserName(),
                    userInfoSaveDto.getUserAge(),
                    userInfoSaveDto.getUserGender(),
                    userInfoSaveDto.getUserRegion(),
                    userInfoSaveDto.getUserProfileImageUrl()
                );
                userRepository.save(selectUser);
            }
        );
        return user.get();
    }

    @Override
    public boolean checkExistUser(UserInfoExistDto userInfoExistDto) {

        try {
            User findUser = userRepository.findByUserEmail(userInfoExistDto.getUserEmail()).get();
            return true;
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

    @Override
    public boolean isValidEmail(String userMail) {

        boolean check = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(userMail);
        if(m.matches()) {
            check = true;
        }
        return check;
    }

    /**
     * <Response>
     *   1. checkSubmitQuestionBook : 사용자의 해당 세부 목표 내 문제집 출제 여부
     *   2. checkSolveQuestionBook : 사용자의 해당 세부 목표 내 문제집 전체 풀이 여부 (푼 개수 == 풀어야 하는 개수)
     *   3. toSolveQuestionBookNum : 사용자의 해당 세부 목표 내 풀어야 할 문제집 개수
     *   4. clearSolveQuestionBookNum : 사용자의 해당 세부 목표 내 문제집 풀이 완료 개수 - 이때, 각 문제집을 풀 때 최소 정답 개수를 충족한 경우
     *   5. achieveGoalStatus : 사용자의 세부 목표 달성 완료 여부
     *    - 세부(이번) 목표 달성 완료 여부는, 출제 + 풀이 활동으로 => 100% 를 채우는 경우
     *    - 문제 풀기 완료는, 전체 문제집을 푼 경우의 상태
     */
    @Override
    public List<StudyGroupListGetResponse> getUserStudyGroupList(String accessToken) {

        Long currentUserId = getCurrentUserId(accessToken);
        User user = userRepository.findById(currentUserId).get();

        // 사용자가 속한 그룹에서, 각 그룹별 진행중인 세부 목표 리스트
        List<Goal> myLatestGoalListPerStudyGroup = getLatestGoalListPerGroup(user);
        List<StudyGroupListGetResponse> studyGroupListGetResponseList = new ArrayList<>();

        for (Goal myGoal : myLatestGoalListPerStudyGroup) {

            StudyGroup myGoalInStudyGroup = myGoal.getStudyGroup();
            studyGroupListGetResponseList.add(
                    // 그룹 정보
                    StudyGroupListGetResponse.builder()
                            .studyGroupListResponse(
                                    StudyGroupListResponse.builder()
                                            .studyGroup(myGoalInStudyGroup)
                                            .build()
                            )
                            // 세부 목표 정보
                            .activeGoalResponse(
                                    ActiveGoalResponse.builder()
                                            .goal(myGoal)
                                            .achieveGoalStatus(getAchieveGoalStatus(user, myGoal))   // 사용자의 해당 세부 목표 달성 여부
                                            .checkSubmitQuestionBook(getCheckSubmitQuestionBook(user, myGoal))   // 사용자의 해당 세부 목표에 문제집 제출 여부
                                            .checkSolveQuestionBook(getCheckCompleteSolveQuestionBook(user, myGoal))   // 사용자의 해당 세부 목표에서 문제 풀기 (전체?) 완료 여부
                                            .clearSolveQuestionBookNum(getClearSolveQuestionBookNum(user, myGoal))   // 풀기를 완료한 문제집 개수
                                            .toSolveQuestionBookNum(getToSolveQuestionBookNum(user, myGoal))   // 사용자가 해당 세부 목표에서 풀어야 하는 문제집 개수
                                            .build()
                            ).build()
            );

        }

        return studyGroupListGetResponseList;
    }

    // 사용자 가입 스터디 그룹의 최근 목표 리스트 조회
    private List<Goal> getLatestGoalListPerGroup(User user) {

        List<UserJoinGroup> userJoinGroupList = user.getUserJoinGroups();

        List<Goal> userLatestGoalListPerStudyGroup = new ArrayList<>();

        for (UserJoinGroup userJoinGroup : userJoinGroupList) {
            StudyGroup myStudyGroup = userJoinGroup.getStudyGroup();
            // 스터디 그룹의 현재 상태가 ACTIVE 이며,
            if (myStudyGroup.getGroupStatus() == StudyGroupStatus.ACTIVE) {
                Goal latestDetailGoal = goalRepository.findFirstByStudyGroupOrderByGoalStartDateDesc(myStudyGroup);
                // 현재 날짜 기준 세부 목표가 진행중인 경우만 추가
                if (latestDetailGoal.getGoalStatus() == GoalStatus.ACTIVE) {
                    userLatestGoalListPerStudyGroup.add(latestDetailGoal);
                }
            }
        }

        return userLatestGoalListPerStudyGroup;
    }


    //TODO 사용자가 해당 세부 목표를 달성하였는지 (100%) 여부
    private boolean getAchieveGoalStatus(User user, Goal goal) {

        boolean checkAchieveGoalStatus = false;
//        UserGoalRate userLatestGoalRate = userGoalRateRepository.findByUser_IdAndGoal_Id(user.getId(), goal.getId()).get();
        Optional<UserGoalRate> userGoalRateOptional = userGoalRateRepository.findByUser_IdAndGoal_Id(user.getId(), goal.getId());
        if (userGoalRateOptional.isPresent()) {
            if (userGoalRateOptional.get().isFinishGoal()) {
                checkAchieveGoalStatus = true;
            }
        }

        return checkAchieveGoalStatus;
    }

    //TODO 사용자가 해당 세부 목표에 문제집을 출제하였는지 여부
    private boolean getCheckSubmitQuestionBook(User user, Goal goal) {

        // 해당 그룹의 세부 목표 중 -> 출제된 문제집에서 -> 출제자 자신의 아이디 존재 ?
        boolean isSubmitCheck = false;
        // 해당 그룹의 세부 목표 중 -> 출제된 문제집 리스트
        List<QuestionBook> questionBookListPerGoal = goal.getQuestionBooks();
        for (QuestionBook questionBook : questionBookListPerGoal) {
            if (user == questionBook.getQuestionBookCreateUser()) {
                isSubmitCheck = true;
            }
        }
        return isSubmitCheck;
    }

    //TODO 사용자의 해당 세부 목표 내 문제 풀이 완료 여부 : 풀어야할 문제집 개수 == 자신이 푼 문제집 개수 ?
    private boolean getCheckCompleteSolveQuestionBook(User user, Goal goal) {

        boolean checkCompleteSolveQuestionBook = false;

        int toSolveQuestionBookNum = getToSolveQuestionBookNum(user, goal);
        int clearSolveQuestionBookNum = getClearSolveQuestionBookNum(user, goal);

        if (toSolveQuestionBookNum == clearSolveQuestionBookNum) {
            checkCompleteSolveQuestionBook = true;
        }
        return checkCompleteSolveQuestionBook;
    }

    //TODO 사용자가 해당 세부 목표 내에서 풀어야 하는 문제집 리스트
    private List<UserSolveQuestionBook> getToSolveQuestionBookList(User user, Goal goal) {

        List<UserSolveQuestionBook> toSolveQuestionBookList = new ArrayList<>();
        List<UserSolveQuestionBook> userSolveQuestionBookList = userSolveQuestionBookRepository.findAllBySolveUser_IdOrderByCreatedDateDesc(user.getId());

        for (UserSolveQuestionBook userSolveQuestionBook : userSolveQuestionBookList) {
            if (userSolveQuestionBook.getSolveQuestionBook().getQuestionBookGoal() == goal) {
                toSolveQuestionBookList.add(userSolveQuestionBook);
            }
        }
        return toSolveQuestionBookList;
    }

    //TODO 사용자가 해당 세부 목표 내에서, 풀어야 하는 문제집 개수
    // 풀어야 하는 문제집 개수 = 해당 세부 목표 내에서 출제된 문제집 수 - 본인이 출제한 문제집 수(1)
    private int getToSolveQuestionBookNum(User user, Goal goal) {

        List<UserSolveQuestionBook> userSolveQuestionBookList = getToSolveQuestionBookList(user, goal);
        int toSolveQuestionBookNum = userSolveQuestionBookList.size();

        for (UserSolveQuestionBook userSolveQuestionBook : userSolveQuestionBookList) {
            // 풀어야 할 문제집 개수 중에서, 본인이 출제한 문제집의 수는 제외하기
            if (userSolveQuestionBook.getSolveQuestionBook().getQuestionBookCreateUser() == user) {
                toSolveQuestionBookNum -= 1;
            }
        }
        return toSolveQuestionBookNum;
    }

    //TODO 해당 세부 목표 내에서 사용자가 풀기를 완료한(각 문제집 당 최소 정답 개수 조건을 충족한) 문제집 개수
    private int getClearSolveQuestionBookNum(User user, Goal goal) {

        int clearQuestionBookNum = 0;
        // 해당 세부 목표(가장 최근 세부 목표) 에 출제된 문제집 개수 중에서, 사용자 본인이 (최소 정답 개수 조건을) 통과한 문제집 개수
        List<UserSolveQuestionBook> userSolveQuestionBookList = getToSolveQuestionBookList(user, goal);
        for (UserSolveQuestionBook userSolveQuestionBook : userSolveQuestionBookList) {
            if (userSolveQuestionBook.isPassed()) {
                clearQuestionBookNum += 1;
            }
        }
        return clearQuestionBookNum;
    }

    private Long getCurrentUserId(String userAccessToken) {
        Long currentUserId = jwtService.getUserId(userAccessToken);
        return currentUserId;
    }
}
