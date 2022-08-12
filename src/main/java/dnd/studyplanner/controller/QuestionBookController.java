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

import dnd.studyplanner.dto.questionbook.request.QuestionBookDto;
import dnd.studyplanner.dto.questionbook.response.UserQuestionBookResponse;
import dnd.studyplanner.dto.response.CustomResponse;
import dnd.studyplanner.service.IQuestionBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/question-book")
@RestController
public class QuestionBookController {

	private final IQuestionBookService questionBookService;

	@PostMapping("/list")
	public ResponseEntity<CustomResponse> addQuestionBookAsList(@RequestBody QuestionBookDto saveDto) {

		List<String> questionList = questionBookService.saveQuestionBook(saveDto);

		return new CustomResponse<>(questionList, SAVE_QUESTION_BOOK_SUCCESS).toResponseEntity();
	}

	@GetMapping("/list/live")
	public ResponseEntity<CustomResponse> getAllUserQuestionBook(
		@RequestHeader("Access-Token") String accessToken
	) {
		List<UserQuestionBookResponse> response = questionBookService.getAllUserQuestionBooks(accessToken);
		return new CustomResponse<>(response).toResponseEntity();
	}


}
