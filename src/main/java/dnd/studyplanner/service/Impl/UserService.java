package dnd.studyplanner.service.Impl;

import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.domain.user.model.UserJoinGroup;
import dnd.studyplanner.dto.studyGroup.response.StudyGroupListResponse;
import dnd.studyplanner.dto.user.request.UserInfoExistDto;
import dnd.studyplanner.dto.user.request.UserInfoSaveDto;
import dnd.studyplanner.jwt.JwtService;
import dnd.studyplanner.repository.StudyGroupRepository;
import dnd.studyplanner.repository.UserJoinGroupRepository;
import dnd.studyplanner.repository.UserRepository;
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
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
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

    @Override
    public List<StudyGroupListResponse> getStudyGroupList(String accessToken) {
        Long currentUserId = getCurrentUserId(accessToken);
        User user = userRepository.findById(currentUserId).get();

        List<UserJoinGroup> userJoinGroupList = user.getUserJoinGroups();
        List<StudyGroup> userStudyGroupList = new ArrayList<>();

        for (UserJoinGroup userJoinGroup : userJoinGroupList) {
            userStudyGroupList.add(userJoinGroup.getStudyGroup());
            log.debug("[GROUP NAME] : {}", userJoinGroup.getStudyGroup().getGroupName());
        }

        List<StudyGroupListResponse> userStudyGroupListResult = userStudyGroupList.stream()
                .map(userGroup -> StudyGroupListResponse.builder()
                    .groupId(userGroup.getId())
                    .groupName(userGroup.getGroupName())
                    .groupStartDate(userGroup.getGroupStartDate())
                    .groupEndDate(userGroup.getGroupEndDate())
                    .groupGoal(userGroup.getGroupGoal())
                    .groupImageUrl(userGroup.getGroupImageUrl())
                    .groupCategory(userGroup.getGroupCategory())
                    .groupStatus(userGroup.getGroupStatus())
                        .build())
                .collect(Collectors.toList());

        return userStudyGroupListResult;
    }

    private Long getCurrentUserId(String userAccessToken) {

        Long currentUserId = jwtService.getUserId(userAccessToken);
        return currentUserId;
    }
}
