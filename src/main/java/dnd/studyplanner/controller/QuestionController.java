package dnd.studyplanner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnd.studyplanner.dto.question.request.QuestionSaveDto;
import dnd.studyplanner.dto.question.request.QuestionSolveDto;
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

	@PostMapping("/solve")
	public ResponseEntity<CustomResponse> checkIsAnswer(
		@RequestHeader("Access-Token") String accessToken,
		QuestionSolveDto solveDto) throws BaseException {
		log.debug("[RequestHeader] : {}", accessToken);
		boolean isCorrectAnswer = questionService.solveQuestion(solveDto, accessToken);

		return new CustomResponse<>(isCorrectAnswer).toResponseEntity();
	}
}
