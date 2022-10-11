package dnd.studyplanner.service.Impl;

import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.goal.model.GoalEndDateComparator;
import dnd.studyplanner.domain.goal.model.GoalStatus;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.studygroup.model.StudyGroupStatus;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.domain.user.model.UserGoalRate;
import dnd.studyplanner.domain.user.model.UserJoinGroup;
import dnd.studyplanner.domain.user.model.UserSolveQuestionBook;
import dnd.studyplanner.dto.goal.response.ActiveGoalResponse;
import dnd.studyplanner.dto.user.request.StudyGroupDetailVersion;
import dnd.studyplanner.dto.user.request.UserInfoExistDto;
import dnd.studyplanner.dto.user.request.UserInfoSaveDto;
import dnd.studyplanner.dto.user.response.UserEmailListResponse;
import dnd.studyplanner.dto.user.response.UserStudyGroupListDetailResponse;
import dnd.studyplanner.dto.user.response.groupAndGoalDetail.StudyGroupDetailResponse;
import dnd.studyplanner.dto.user.response.groupAndGoalDetail.StudyGroupGoalResponse;
import dnd.studyplanner.dto.user.response.groupList.StudyGroupListGetResponse;
import dnd.studyplanner.dto.user.response.groupList.StudyGroupListResponse;
import dnd.studyplanner.dto.user.response.usergoal.StudyGroupResponse;
import dnd.studyplanner.dto.user.response.usergoal.UserGoalListResponse;
import dnd.studyplanner.dto.user.response.usergoal.UserGoalResponse;
import dnd.studyplanner.dto.user.response.versionDetail.StudyGroupAndGoalDetailPersonalVerResponse;
import dnd.studyplanner.dto.user.response.versionDetail.StudyGroupAndGoalDetailTeamVerResponse;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.jwt.JwtService;
import dnd.studyplanner.repository.GoalRepository;
import dnd.studyplanner.repository.QuestionBookRepository;
import dnd.studyplanner.repository.StudyGroupRepository;
import dnd.studyplanner.repository.UserGoalRateRepository;
import dnd.studyplanner.repository.UserJoinGroupRepository;
import dnd.studyplanner.repository.UserRepository;
import dnd.studyplanner.repository.UserSolveQuestionBookRepository;
import dnd.studyplanner.service.IUserRateService;
import dnd.studyplanner.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final UserJoinGroupRepository userJoinGroupRepository;
    private final GoalRepository goalRepository;
    private final UserSolveQuestionBookRepository userSolveQuestionBookRepository;
    private final UserGoalRateRepository userGoalRateRepository;
    private final QuestionBookRepository questionBookRepository;

    private final IUserRateService userRateService;

    private final JwtService jwtService;

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
        /**
         * 각 활동중인 그룹의 정보는 무조건 포함
         * -> 각 활동중인 그룹 내의 세부 목표 중에서, 현재 진행중인 세부 목표가 하나도 없는 경우는 -> 그룹의 정보만 포함
         * -> 각 활동중인 그룹 내의 세부 목표 중에서, 현재 진행중이 세부 목표가 있는 경우 -> 그룹의 정보 + 가장 종료일이 임박한 세부 목표의 정부 포함
         */
        List<StudyGroupListGetResponse> studyGroupListGetResponseList = new ArrayList<>();   // RESPONSE LIST

        List<StudyGroup> myActiveStudyGroupList = getActiveStudyGroupList(user);   // 현재 활동중인 스터디 그룹

//        List<Goal> userLatestGoalListPerStudyGroup = new ArrayList<>();
        List<Goal> userActiveGoalListPerStudyGroup = new ArrayList<>();   // 각 그룹 당 현재 진행중인 목표들의 리스트
        for (StudyGroup studyGroup : myActiveStudyGroupList) {
            List<Goal> groupGoalList = studyGroup.getGroupDetailGoals();

            for (Goal detailGoal : groupGoalList) {
                if (detailGoal.getGoalStatus().equals(GoalStatus.ACTIVE)) {
                    userActiveGoalListPerStudyGroup.add(detailGoal);   // 각 그룹 당 현재 진행중인 목표들의 리스트
                }
            }

            // 각 그룹 내에서 세부 목표 리스트마다 정렬 수행
            Collections.sort(userActiveGoalListPerStudyGroup, new GoalEndDateComparator());

            // 종료 날짜가 가장 임박한 한 개의 목표만 리스트에 추가 -> 수정 후 : 여러 개 유지
//            if (!userActiveGoalListPerStudyGroup.isEmpty()) {
//                userLatestGoalListPerStudyGroup.add(userActiveGoalListPerStudyGroup.get(0));
//            }
        }

        for (Goal activeGoal : userActiveGoalListPerStudyGroup) {
            StudyGroup myActiveStudyGroup = activeGoal.getStudyGroup();
            studyGroupListGetResponseList.add(
                    StudyGroupListGetResponse.builder()
                            .studyGroupListResponse(
                                    StudyGroupListResponse.builder()
                                            .studyGroup(myActiveStudyGroup)
                                            .build()
                            )
                            .activeGoalResponse(
                                    ActiveGoalResponse.builder()
                                            .goal(activeGoal)
                                            .achieveGoalStatus(getAchieveGoalStatus(user, activeGoal))   // 사용자의 해당 세부 목표 달성 여부
                                            .checkSubmitQuestionBook(getCheckSubmitQuestionBook(user, activeGoal))   // 사용자의 해당 세부 목표에 문제집 제출 여부
                                            .checkSolveQuestionBook(getCheckCompleteSolveQuestionBook(user, activeGoal))   // 사용자의 해당 세부 목표에서 문제 풀기 (전체?) 완료 여부
                                            .clearSolveQuestionBookNum(getClearSolveQuestionBookNum(user, activeGoal))   // 풀기를 완료한 문제집 개수
                                            .toSolveQuestionBookNum(getToSolveQuestionBookNum(user, activeGoal))   // 사용자가 해당 세부 목표에서 풀어야 하는 문제집 개수
                                            .questionPerQuestionBook(activeGoal.getQuestionPerQuestionBook()) // 세부 목표에 대한 문제집 별 출제 문제 개수 (문제 출제 시 필요한 정보)
                                            .build()
                            )
                            .build()

            );
        }

        return studyGroupListGetResponseList;
    }

    /**
     *
     */
    @Override
    public UserGoalListResponse getUserStudyGroupListV2(String accessToken) throws BaseException {

        Long currentUserId = getCurrentUserId(accessToken);
        User user = userRepository.findById(currentUserId).orElseThrow(
            () -> new BaseException(NOT_VALID_USER)
        );

        List<StudyGroup> studyGroups = userJoinGroupRepository.findByUserId(currentUserId)
            .stream()
            .map(UserJoinGroup::getStudyGroup)
            .filter(s -> s.getGroupStatus() != StudyGroupStatus.COMPLETE)
            .collect(Collectors.toList());

        List<StudyGroupResponse> emptyGoalStudyGroup = new ArrayList<>();
        List<Long> activeStudyGroups = new ArrayList<>();

        for (StudyGroup studyGroup : studyGroups) {
            if (studyGroup.getGroupDetailGoals().isEmpty()) {
                emptyGoalStudyGroup.add(
                    StudyGroupResponse.builder()
                        .studyGroupCategory(studyGroup.getGroupCategory())
                        .studyGroupStatus(studyGroup.getGroupStatus())
                        .studyGroupContent(studyGroup.getGroupName())
                        .studyGroupEndDate(studyGroup.getGroupEndDate())
                        .build()
                );
                continue;
            }

            activeStudyGroups.add(studyGroup.getId());
        }

        List<Goal> goals = goalRepository.findAllByStudyGroupIdInOrderByGoalEndDate(
            activeStudyGroups);

        List<UserGoalResponse> userGoalResponseList = goals.stream()
            .filter(g -> g.getGoalStatus() != GoalStatus.COMPLETE)
            .map(g -> UserGoalResponse.builder()
                .studyGroupCategory(g.getStudyGroup().getGroupCategory())
                .studyGroupId(g.getStudyGroup().getId())
                .goalId(g.getId())
                .studyGroupStatus(g.getStudyGroup().getGroupStatus())
                .goalStatus(g.getGoalStatus())
                .goalContent(g.getGoalContent())
                .studyGroupContent(g.getStudyGroup().getGroupName())
                .questionBookSubmitted(userSubmittedQuestionBook(user, g))
                .groupEndDate(g.getGoalEndDate())
                .build()
            ).collect(Collectors.toList());

        return UserGoalListResponse.builder()
            .userNickname(user.getUserNickName())
            .userGoalResponseList(userGoalResponseList)
            .emptyGoalStudyGroup(emptyGoalStudyGroup)
            .build();
    }

    private boolean userSubmittedQuestionBook(User user, Goal goal) {
        return questionBookRepository.existsByQuestionBookCreateUserAndQuestionBookGoal(user, goal);
    }

    @Override
    public UserStudyGroupListDetailResponse getUserStudyGroupListDetail(String accessToken, Long groupId, Long goalId, String version) throws BaseException {
        /**
         * goalId 의 Default 값은 가장 최근 세부 목표
         * version 의 Default 값은 PERSONAL
         */
        Long currentUserId = getCurrentUserId(accessToken);
        User user = userRepository.findById(currentUserId).get();
        StudyGroup studyGroup = studyGroupRepository.findById(groupId).get();

        // TODO 사용자가 입력받은 스터디 그룹에 존재하는지 확인
        if (!userJoinGroupRepository.existsByUserAndStudyGroup(user, studyGroup)) {
            throw new BaseException(USER_NOT_IN_GROUP);
        }

        // 선택한 StudyGroup 에 존재하는 세부 목표 최근 순서에 따른 리스트
        List<Goal> detailGoalList = goalRepository.findAllByStudyGroupOrderByCreatedDateDesc(studyGroup);

        // 선택한 StudyGroup 의 가장 최근 세부 목표
        // TODO detailGoalList null 체크
        Goal defaultGoal = detailGoalList.get(0);
        Goal targetGoal = null;
        if (goalId == null) {
            targetGoal = defaultGoal;
        } else {
            targetGoal = goalRepository.findById(goalId).get();   // 파라미터로 전달되는 goal ID -> 각 세부 목표별 문제집 조회를 위함
        }
        log.debug("[문제집 리스트를 보여줄 세부 목표 ID] : {}", targetGoal.getId());

        List<QuestionBook> targetQuestionBookList = targetGoal.getQuestionBooks();
        List<QuestionBook> defaultQuestionBookList = defaultGoal.getQuestionBooks();

        if (version == null) {
            version = "PERSONAL";
        }
        StudyGroupDetailVersion studyGroupDetailVersion = StudyGroupDetailVersion.valueOf(version.toUpperCase());
        log.debug("[문제집 리스트를 보여줄 버전] : {}", studyGroupDetailVersion);

        List<String> invitedUserNameList = new ArrayList<>();
        List<UserJoinGroup> userJoinGroupList = studyGroup.getUserJoinGroups();
        log.debug("[그룹 구성 인원 수] : {}", userJoinGroupList.size());
        for (UserJoinGroup userJoinGroup : userJoinGroupList) {
            String userName = userJoinGroup.getUser().getUserNickName();
            log.debug("[그룹 구성원 이름] : {}", userName);
            invitedUserNameList.add(userName);
        }

        List<StudyGroupGoalResponse> studyGroupGoalResponseList = detailGoalList.stream()
                .map(tmpGoal -> StudyGroupGoalResponse.builder()
                        .goal(tmpGoal)
                        .achieveRate(getGoalAchieveRate(user, tmpGoal))
                        .build())
                .collect(Collectors.toList());

        // 스터디 그룹 및 세부 목표
        StudyGroupDetailResponse studyGroupDetailResponse = StudyGroupDetailResponse.builder()
                .studyGroup(studyGroup)
                .invitedUserNameList(invitedUserNameList)
                .studyGroupGoalResponseList(studyGroupGoalResponseList)
                .build();

        // 세부 목표 ID 하나에 존재하는 문제집 리스트들에 대한 각 정보
        List<StudyGroupAndGoalDetailPersonalVerResponse> studyGroupAndGoalDetailPersonalVerResponseList = new ArrayList<>();

        // TODO 개인 Ver - 가장 최근 Goal ID (defaultGoal.getId()) 를 기준으로 Goal ID 설정
        Long defaultGoalId = defaultGoal.getId();
        if (goalId == null) {
            for (QuestionBook questionBook : defaultQuestionBookList) {
                StudyGroupAndGoalDetailPersonalVerResponse studyGroupAndGoalDetailPersonalVerResponse = StudyGroupAndGoalDetailPersonalVerResponse
                        .builder()
                        .goal(defaultGoal)
                        .myQuestionBook(getCheckMyQuestionBook(user, questionBook))
                        .checkEditEnabled(getCheckEditEnabled(user, questionBook))   // getCheckEditEnabled()
                        .questionBook(questionBook)
                        .questionNumPerQuestionBook(questionBook.getQuestionBookQuestionNum())   // 각 문제집 당 문제 수
                        .answerNumPerQuestionBook(getAnswerNumPerQuestionBook(user, questionBook))   // 사용자의 각 문제집 풀이에 따른 정답 개수 -> 푼 경우에만
                        .checkCompleteToSolve(getCheckCompleteSolveQuestionBook(user, defaultGoal))   // 풀었는지 여부
                        .build();

                studyGroupAndGoalDetailPersonalVerResponseList.add(studyGroupAndGoalDetailPersonalVerResponse);
            }

        } else {
            // 세부 목표 ID 별로 존제하는 문제집 하나의 정보 -> 리스트로 만들어 세부 목표에 대한 문제집 리스트로 처리
            for (QuestionBook questionBook : targetQuestionBookList) {
                StudyGroupAndGoalDetailPersonalVerResponse studyGroupAndGoalDetailPersonalVerResponse = StudyGroupAndGoalDetailPersonalVerResponse
                        .builder()
                        .goal(targetGoal)
                        .myQuestionBook(getCheckMyQuestionBook(user, questionBook))
                        .checkEditEnabled(getCheckEditEnabled(user, questionBook))   // getCheckEditEnabled()
                        .questionBook(questionBook)
                        .questionNumPerQuestionBook(questionBook.getQuestionBookQuestionNum())   // 각 문제집 당 문제 수
                        .answerNumPerQuestionBook(getAnswerNumPerQuestionBook(user, questionBook))   // 사용자의 각 문제집 풀이에 따른 정답 개수 -> 푼 경우에만
                        .checkCompleteToSolve(getCheckCompleteSolveQuestionBook(user, targetGoal))
                        .build();

                studyGroupAndGoalDetailPersonalVerResponseList.add(studyGroupAndGoalDetailPersonalVerResponse);
            }
        }

        // TODO 팀 Ver
        List<StudyGroupAndGoalDetailTeamVerResponse> studyGroupAndGoalDetailTeamVerResponseList = new ArrayList<>();
        for (QuestionBook questionBook : targetQuestionBookList) {
            List<String> peopleNameToSolveList = getPersonNameListToSolveQuestionBook(questionBook);
            StudyGroupAndGoalDetailTeamVerResponse studyGroupAndGoalDetailTeamVerResponse = StudyGroupAndGoalDetailTeamVerResponse
                    .builder()
                    .goal(targetGoal)
                    .questionBook(questionBook)
                    .personListOfCompleteToSolvePerQuestionBook(peopleNameToSolveList)   // 어떤 사람들이 각 문제집을 풀었는지
                    .personNumOfCompleteToSolvePerQuestionBook(peopleNameToSolveList.size())   // 몇 명의 사람들이 각 문제집을 풀었는지
                    .build();

            studyGroupAndGoalDetailTeamVerResponseList.add(studyGroupAndGoalDetailTeamVerResponse);
        }

        if ("PERSONAL".equals(studyGroupDetailVersion.toString())) {
            return UserStudyGroupListDetailResponse.builder()
                    .studyGroupDetailResponse(studyGroupDetailResponse)
                    .studyGroupAndGoalDetailPersonalVerResponseList(studyGroupAndGoalDetailPersonalVerResponseList)
                    .build();
        } else {
            return UserStudyGroupListDetailResponse.builder()
                    .studyGroupDetailResponse(studyGroupDetailResponse)
                    .studyGroupAndGoalDetailTeamVerResponseList(studyGroupAndGoalDetailTeamVerResponseList)
                    .build();
        }
    }

    // 현재 활동중인 스터디 그룹 리스트 조회 - 사용
    private List<StudyGroup> getActiveStudyGroupList(User user) {
        List<StudyGroup> myActiveStudyGroup = new ArrayList<>();

        for (UserJoinGroup userJoinGroup : user.getUserJoinGroups()) {
            StudyGroup studyGroup = userJoinGroup.getStudyGroup();
            if (studyGroup.getGroupStatus() == StudyGroupStatus.ACTIVE) {
                myActiveStudyGroup.add(studyGroup);
            }
        }
        return myActiveStudyGroup;
    }

    // 현재 할동중인 스터디 그룹 중, 각 스터디 그룹마다 현재 활동중인 목표가 있는지에 대한 리스트 조회 - 사용
    private Goal getActiveGoalInStudyGroup(StudyGroup studyGroup) {

        List<Goal> userActiveGoalListPerStudyGroup = new ArrayList<>();   // 각 그룹 당 현재 진행중인 목표들의 리스트

        List<Goal> myGoalListPerGroup = studyGroup.getGroupDetailGoals();
        for (Goal goal : myGoalListPerGroup) {
            if (goal.getGoalStatus().equals(GoalStatus.ACTIVE)) {
                userActiveGoalListPerStudyGroup.add(goal);   // 각 그룹 당 현재 진행중인 목표들의 리스트
            }
        }
        if (userActiveGoalListPerStudyGroup.isEmpty()) {
            return null;
        }
        Collections.sort(userActiveGoalListPerStudyGroup, new GoalEndDateComparator());

        return userActiveGoalListPerStudyGroup.get(0);
    }


    // 각 문제집을 푼 그룹의 구성원 이름
    private List<String> getPersonNameListToSolveQuestionBook(QuestionBook questionBook) {

        List<String> personNameListToSolveQuestionBook = new ArrayList<>();
        try {
            List<UserSolveQuestionBook> userSolveQuestionBookList = questionBook.getUserSolveQuestionBooks();
            for (UserSolveQuestionBook userSolveQuestionBook : userSolveQuestionBookList) {
                String name = userSolveQuestionBook.getSolveUser().getUserNickName();
                personNameListToSolveQuestionBook.add(name);
            }
        } catch (NoSuchElementException e) {

        }
        return personNameListToSolveQuestionBook;
    }

    // 사용자가 푼 문제집에서 정답을 맞춘 개수
    private int getAnswerNumPerQuestionBook(User user, QuestionBook questionBook) {

        int answerNumPerQuestionBook = 0;
        List<UserSolveQuestionBook> userSolveQuestionBookList = questionBook.getUserSolveQuestionBooks();
        for (UserSolveQuestionBook userSolveQuestionBook : userSolveQuestionBookList) {
            if (userSolveQuestionBook.getSolveUser().getId().equals(user.getId())) {
                answerNumPerQuestionBook = userSolveQuestionBook.getAnswerNum();
            }
        }
        return answerNumPerQuestionBook;
    }

    private boolean getCheckMyQuestionBook(User user, QuestionBook questionBook) {
        boolean checkMyQuestionBook = false;
        if (questionBook.getQuestionBookCreateUser().getId().equals(user.getId())) {
            checkMyQuestionBook = true;
        }
        return checkMyQuestionBook;
    }

    // 사용자 자신이 출제한 문제집을 수정할 수 있는지 여부 확인
    // 해당 문제집을 푼 사람이 한 명이라도 존재하는 경우, 수정 불가
    // 조건1 : 내가 출제한 문제집인지 / 조건2 : 해당 문제집을 푼 사용자가 아무도 없는 경우
    private boolean getCheckEditEnabled(User user, QuestionBook questionBook) {

        boolean checkEditEnabled = true;
        // 그룹원들의 문제집 풀이 현황 - 문제를 푼 구성원이 존재하는지 확인
        List<UserSolveQuestionBook> userSolveQuestionBookList = questionBook.getUserSolveQuestionBooks();
        for (UserSolveQuestionBook userSolveQuestionBook : userSolveQuestionBookList) {
            if (userSolveQuestionBook.isSolved()) {
                checkEditEnabled = false;
                break;
            }
        }
        // 사용자 본인이 출제한 문제집인지 확인 - if not false
        if (!getCheckMyQuestionBook(user, questionBook)) {
            checkEditEnabled = false;
        }
        return checkEditEnabled;
    }

    // 사용자의 각 세부 목표별 달성률
    private int getGoalAchieveRate(User user, Goal goal) {

        Optional<UserGoalRate> userGoalRateOptional = userGoalRateRepository.findByUser_IdAndGoal_Id(user.getId(), goal.getId());
        if (userGoalRateOptional.isPresent()) {
            return userGoalRateOptional.get().getAchieveRate();
        }
        return 0;
    }


    //TODO 사용자가 해당 세부 목표를 달성하였는지 (100%) 여부
    public boolean getAchieveGoalStatus(User user, Goal goal) {

        boolean checkAchieveGoalStatus = false;
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
    public int getToSolveQuestionBookNum(User user, Goal goal) {

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
    public int getClearSolveQuestionBookNum(User user, Goal goal) {

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

    @Override
    public UserEmailListResponse getUserEmailList(String userEmail) {

        List<String> userEmailList = new ArrayList<>();
        List<User> userList = userRepository.findByUserEmailContaining(userEmail);

        for (User user : userList) {
            userEmailList.add(user.getUserEmail());
        }
        UserEmailListResponse userEmailListResponse = UserEmailListResponse.builder()
                .userEmailList(userEmailList)
                .build();

        return userEmailListResponse;
    }
}