package dnd.studyplanner.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnd.studyplanner.dto.question.request.QuestionSaveDto;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.service.QuestionService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/question")
@RestController
public class QuestionController {

	private final QuestionService questionService;

	@PostMapping
	public void addQuestion(QuestionSaveDto saveDto) throws BaseException {
		questionService.saveQuestion(saveDto);
	}
}
