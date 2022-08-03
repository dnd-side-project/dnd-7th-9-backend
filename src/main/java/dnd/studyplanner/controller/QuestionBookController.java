package dnd.studyplanner.controller;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnd.studyplanner.config.Message;
import dnd.studyplanner.config.StatusCode;
import dnd.studyplanner.dto.questionbook.request.QuestionBookDto;
import dnd.studyplanner.dto.questionbook.request.QuestionBookSaveDto;
import dnd.studyplanner.service.IQuestionBookService;
import dnd.studyplanner.service.Impl.QuestionBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/question-book")
@RestController
public class QuestionBookController {

	private final IQuestionBookService questionBookService;

	@PostMapping("/list")
	public ResponseEntity<Message> addQuestionBookAsList(@RequestBody QuestionBookDto saveDto) {

		List<String> questionList = questionBookService.saveQuestionBook(saveDto);

		Message message = new Message();
		HttpHeaders headers= new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

		message.setStatus(StatusCode.OK);
		message.setMessage("문제집 저장 성공");
		message.setData(questionList);

		return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}


}
