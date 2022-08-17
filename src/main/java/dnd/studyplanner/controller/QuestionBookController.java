package dnd.studyplanner.controller;

import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnd.studyplanner.domain.user.model.UserGoalRate;
import dnd.studyplanner.dto.questionbook.request.QuestionBookDto;
import dnd.studyplanner.dto.questionbook.request.QuestionBookSolveDto;
import dnd.studyplanner.dto.questionbook.response.QuestionBookSaveResponse;
import dnd.studyplanner.dto.questionbook.response.QuestionBookSolveResponse;
import dnd.studyplanner.dto.questionbook.response.UserQuestionBookResponse;
import dnd.studyplanner.dto.response.CustomResponse;
import dnd.studyplanner.service.IQuestionBookService;
import dnd.studyplanner.service.IUserRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/question-book")
@RestController
public class QuestionBookController {

	private final IQuestionBookService questionBookService;
	private final IUserRateService userGoalRateService;

	@PostMapping("/list")
	public ResponseEntity<CustomResponse> addQuestionBookAsList(
		@RequestHeader("Access-Token") String accessToken,
		@RequestBody QuestionBookDto saveDto
	) {

		questionBookService.saveQuestionBook(accessToken, saveDto);
		UserGoalRate userGoalRate = userGoalRateService.updatePostQuestionBook(accessToken, saveDto.getGoalId());
		QuestionBookSaveResponse response = QuestionBookSaveResponse.builder()
			.addedRate(50)
			.questionBookPostRate(userGoalRate.getPostRate())
			.questionBookSolveRate(userGoalRate.getSolveRate())
			.userTotalRate(userGoalRate.getAchieveRate())
			.build();

		return new CustomResponse<>(response, SAVE_QUESTION_BOOK_SUCCESS).toResponseEntity();
	}

	@GetMapping("/list/live")
	public ResponseEntity<CustomResponse> getAllUserQuestionBook(
		@RequestHeader("Access-Token") String accessToken
	) {
		List<UserQuestionBookResponse> response = questionBookService.getAllUserQuestionBooks(accessToken);
		return new CustomResponse<>(response).toResponseEntity();
	}

	@PostMapping("/end")
	public ResponseEntity<CustomResponse> finishQuestionBook(
		@RequestHeader("Access-Token") String accessToken,
		@RequestBody QuestionBookSolveDto requestDto
	) {
		boolean passQuestionBook = questionBookService.solveQuestionBook(accessToken, requestDto);
		UserGoalRate userGoalRate = userGoalRateService.getUserGoalRateByQuestionBookId(accessToken,
			requestDto.getQuestionBookId());
		if (!passQuestionBook) {
			QuestionBookSolveResponse response = QuestionBookSolveResponse.builder()
				.isPass(false)
				.addedRate(0)
				.userTotalRate(userGoalRate.getAchieveRate())
				.questionBookPostRate(userGoalRate.getPostRate())
				.questionBookSolveRate(userGoalRate.getSolveRate())
				.build();
			return new CustomResponse<>(response).toResponseEntity();
		}
		int beforeUpdate = userGoalRate.getAchieveRate();

		userGoalRateService.updateAfterQuestionBook(userGoalRate);

		int afterUpdate = userGoalRate.getAchieveRate();
		QuestionBookSolveResponse response = QuestionBookSolveResponse.builder()
			.isPass(true)
			.addedRate(afterUpdate - beforeUpdate)
			.userTotalRate(userGoalRate.getAchieveRate())
			.questionBookPostRate(userGoalRate.getPostRate())
			.questionBookSolveRate(userGoalRate.getSolveRate())
			.build();

		return new CustomResponse<>(response).toResponseEntity();
	}

}
