package dnd.studyplanner.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnd.studyplanner.dto.questionbook.request.QuestionBookDto;
import dnd.studyplanner.dto.questionbook.request.QuestionBookSaveDto;
import dnd.studyplanner.service.QuestionBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/question-book")
@RestController
public class QuestionBookController {

	private final QuestionBookService questionBookService;

	@PostMapping
	public Long addQuestionBook(QuestionBookSaveDto saveDto) {
		// 1. 검증

		// questionBookService.saveQuestionBook(saveDto);
		return 1L;
	}

	@PostMapping("/list")
	public Long addQuestionBookAsList(@RequestBody QuestionBookDto saveDto) {

		questionBookService.saveQuestionBook(saveDto);
		return 1L;
	}


}
