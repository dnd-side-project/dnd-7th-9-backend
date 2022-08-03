package dnd.studyplanner.service;

import java.util.List;

import dnd.studyplanner.dto.questionbook.request.QuestionBookDto;

public interface IQuestionBookService {

	List<String> saveQuestionBook(QuestionBookDto saveDto);
}
