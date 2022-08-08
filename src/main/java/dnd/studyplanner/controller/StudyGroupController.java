package dnd.studyplanner.controller;

import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.dto.response.CustomResponse;
import dnd.studyplanner.dto.studyGroup.request.StudyGroupSaveDto;
import dnd.studyplanner.service.IStudyGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dnd.studyplanner.dto.response.CustomResponseStatus.SAVE_GROUP_SUCCESS;

@RequiredArgsConstructor
@RequestMapping("/group")
@RestController
public class StudyGroupController {

    private final IStudyGroupService studyGroupService;

    // 그룹 생성
    @PostMapping("/info")
    public ResponseEntity<CustomResponse> addStudyGroup(
            @RequestHeader(value = "Access-Token") String accessToken,
            @RequestBody StudyGroupSaveDto studyGroupSaveDto) {

        StudyGroup updateStudyGroup = studyGroupService.saveStudyGroup(studyGroupSaveDto, accessToken);

        return new CustomResponse<>(updateStudyGroup, SAVE_GROUP_SUCCESS).toResponseEntity();

    }

}
