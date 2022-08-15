package dnd.studyplanner.service;

import java.util.List;

import dnd.studyplanner.dto.questionbook.request.QuestionBookDto;
import dnd.studyplanner.dto.questionbook.request.SolveQuestionBookDto;
import dnd.studyplanner.dto.questionbook.response.UserQuestionBookResponse;

public interface IQuestionBookService {

	List<String> saveQuestionBook(String accessToken, QuestionBookDto saveDto);

	List<UserQuestionBookResponse> getAllUserQuestionBooks(String accessToken);

	boolean isPassQuestionBook(String accessToken, SolveQuestionBookDto requestDto);

	public int getRecentQuestionBookCount(Long userId, Long goalId);
}
