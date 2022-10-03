package dnd.studyplanner.controller;

import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import dnd.studyplanner.dto.questionbook.response.UserSolveQuestionResponse;
import dnd.studyplanner.dto.response.CustomResponse;
import dnd.studyplanner.exception.BaseException;
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

		try {
			questionBookService.saveQuestionBook(accessToken, saveDto);
			UserGoalRate userGoalRate = userGoalRateService.updatePostQuestionBook(accessToken, saveDto.getGoalId());
			QuestionBookSaveResponse response = QuestionBookSaveResponse.builder()
				.addedRate(50)
				.questionBookPostRate(userGoalRate.getPostRate())
				.questionBookSolveRate(userGoalRate.getSolveRate())
				.userTotalRate(userGoalRate.getAchieveRate())
				.build();
			return new CustomResponse<>(response, SAVE_QUESTION_BOOK_SUCCESS).toResponseEntity();
		} catch (BaseException e) {
			return new CustomResponse<>(e.getStatus()).toResponseEntity();
		}

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

		try {
			if (questionBookService.isSolvedQuestionBook(accessToken, requestDto.getQuestionBookId())) {
				return new CustomResponse<>(ALREADY_SOLVED_QUESTION_BOOK).toResponseEntity();
			}
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
		} catch (BaseException e) {
			return new CustomResponse<>(e.getStatus()).toResponseEntity();
		}

	}

	@GetMapping("/{questionBookId}/solved/details")
	public ResponseEntity<CustomResponse> getSolveQuestionBookDetail(
		@RequestHeader("Access-Token") String accessToken,
		@PathVariable Long questionBookId
	) {
		List<UserSolveQuestionResponse> response = questionBookService.getUserSolveDetails(accessToken,
			questionBookId);

		return new CustomResponse<>(response).toResponseEntity();
	}

	@PutMapping("/{questionBookId}/edit")
	public ResponseEntity<CustomResponse> editQuestionBook(
		@RequestHeader("Access-Token") String accessToken,
		@PathVariable Long questionBookId,
		@RequestBody QuestionBookDto saveDto
	) {
		try {
			questionBookService.editQuestionBook(accessToken, questionBookId, saveDto);
			return new CustomResponse<>(EDIT_QUESTION_BOOK_SUCCESS).toResponseEntity();
		} catch (BaseException e) {
			return new CustomResponse<>(e.getStatus()).toResponseEntity();
		}

	}

}
