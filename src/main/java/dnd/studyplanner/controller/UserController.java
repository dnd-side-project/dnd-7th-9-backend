package dnd.studyplanner.controller;

import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.dto.response.CustomResponse;
import dnd.studyplanner.dto.user.request.UserInfoSaveDto;
import dnd.studyplanner.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dnd.studyplanner.dto.response.CustomResponseStatus.SAVE_USER_SUCCESS;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final IUserService userService;

    @PostMapping("/info")
    public ResponseEntity<CustomResponse> addUserInfo(
            @RequestHeader(value = "Access-Token") String accessToken,
            @RequestBody UserInfoSaveDto userInfoSaveDto) {

        log.debug("[RequestHeader] : {}", accessToken);
        User updateUser = userService.saveUserInfo(userInfoSaveDto, accessToken);

        return new CustomResponse<>(updateUser, SAVE_USER_SUCCESS).toResponseEntity();
    }

}
