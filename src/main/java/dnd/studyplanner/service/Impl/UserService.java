package dnd.studyplanner.service.Impl;

import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.dto.user.request.UserInfoSaveDto;
import dnd.studyplanner.jwt.JwtService;
import dnd.studyplanner.repository.UserRepository;
import dnd.studyplanner.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    private Long getCurrentUserId(String userAccessToken) {

        Long currentUserId = jwtService.getUserId(userAccessToken);
        return currentUserId;
    }
}
