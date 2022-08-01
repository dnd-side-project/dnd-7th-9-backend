package dnd.studyplanner.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import dnd.studyplanner.dto.questionbook.request.QuestionBookSaveDto;
import dnd.studyplanner.service.QuestionBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController("/question-book")
public class QuestionBookController {

	private final QuestionBookService questionBookService;

	@PostMapping
	public Long addQuestionBook(QuestionBookSaveDto saveDto) {
		// 1. 검증

		// questionBookService.saveQuestionBook(saveDto);
		return 1L;
	}


}
