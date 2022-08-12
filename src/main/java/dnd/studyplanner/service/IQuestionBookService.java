package dnd.studyplanner.service;

import java.util.List;

import dnd.studyplanner.dto.questionbook.request.QuestionBookDto;
import dnd.studyplanner.dto.questionbook.response.UserQuestionBookResponse;

public interface IQuestionBookService {

	List<String> saveQuestionBook(QuestionBookDto saveDto);

	List<UserQuestionBookResponse> getAllUserQuestionBooks(String accessToken);
}
