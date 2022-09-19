package dnd.studyplanner.controller;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.dto.goal.request.GoalSaveDto;
import dnd.studyplanner.dto.goal.response.GoalSaveResponse;
import dnd.studyplanner.dto.response.CustomResponse;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.service.IGoalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dnd.studyplanner.dto.response.CustomResponseStatus.SAVE_GOAL_SUCCESS;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/goal")
@RestController
public class GoalController {

    private final IGoalService goalService;

    @PostMapping("/save")
    public ResponseEntity<CustomResponse> addPeriodGoal(
            @RequestHeader(value = "Access-Token") String accessToken,
            @RequestBody GoalSaveDto goalSaveDto) {

        try {
            GoalSaveResponse updateGoal = goalService.addDetailGoal(accessToken, goalSaveDto);
            return new CustomResponse<>(updateGoal, SAVE_GOAL_SUCCESS).toResponseEntity();
        } catch (BaseException e) {
            return new CustomResponse<>(e.getStatus()).toResponseEntity();
        }
    }
}
