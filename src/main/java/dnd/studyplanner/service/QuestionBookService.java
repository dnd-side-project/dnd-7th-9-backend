package dnd.studyplanner.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dnd.studyplanner.dto.question.request.QuestionListDto;
import dnd.studyplanner.dto.questionbook.request.QuestionBookSaveDto;
import dnd.studyplanner.repository.QuestionBookRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class QuestionBookService {

	private final QuestionBookRepository questionBookRepository;


	public void saveQuestionBook(QuestionListDto saveDto) {

	}
}
