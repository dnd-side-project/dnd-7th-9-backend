package dnd.studyplanner.controller;

import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnd.studyplanner.dto.question.request.QuestionSaveDto;
import dnd.studyplanner.dto.question.request.QuestionSolveDto;
import dnd.studyplanner.dto.question.response.QuestionListResponseDto;
import dnd.studyplanner.dto.question.response.QuestionResponseDto;
import dnd.studyplanner.dto.response.CustomResponse;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.service.IQuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/question")
@RestController
public class QuestionController {

	private final IQuestionService questionService;

	@PostMapping
	public void addQuestion(QuestionSaveDto saveDto) throws BaseException {
		questionService.saveQuestion(saveDto);
	}

	@GetMapping("/{questionBookId}")
	public ResponseEntity<CustomResponse> getQuestionsByQuestionBookId(
		@PathVariable Long questionBookId
	) {
		try {
			QuestionListResponseDto response = questionService.getQuestions(questionBookId);
			return new CustomResponse<>(response).toResponseEntity();
		} catch (BaseException e) {
			return new CustomResponse<>(e.getStatus()).toResponseEntity();
		}
	}

	@PostMapping("/solve")
	public ResponseEntity<CustomResponse> checkIsAnswer(
		@RequestHeader("Access-Token") String accessToken,
		@RequestBody QuestionSolveDto solveDto) {
		log.debug("[RequestHeader] : {}", accessToken);

		try {
			boolean isCorrectAnswer = questionService.solveQuestion(solveDto, accessToken);
			return new CustomResponse<>(isCorrectAnswer).toResponseEntity();
		} catch (BaseException e) {
			return new CustomResponse<>(NOT_EXIST_DATA).toResponseEntity();
		}
	}

}
