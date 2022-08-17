package dnd.studyplanner.service;

import java.util.List;

import dnd.studyplanner.dto.questionbook.request.QuestionBookDto;
import dnd.studyplanner.dto.questionbook.request.QuestionBookSolveDto;
import dnd.studyplanner.dto.questionbook.response.UserQuestionBookResponse;

public interface IQuestionBookService {

	List<String> saveQuestionBook(String accessToken, QuestionBookDto saveDto);

	List<UserQuestionBookResponse> getAllUserQuestionBooks(String accessToken);

	boolean solveQuestionBook(String accessToken, QuestionBookSolveDto requestDto);

	public int getRecentQuestionBookCount(Long userId, Long goalId);
}
