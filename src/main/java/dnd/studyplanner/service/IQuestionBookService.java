package dnd.studyplanner.service;

import java.util.List;

import dnd.studyplanner.domain.user.model.UserSolveQuestion;
import dnd.studyplanner.dto.questionbook.request.QuestionBookDto;
import dnd.studyplanner.dto.questionbook.request.QuestionBookSolveDto;
import dnd.studyplanner.dto.questionbook.response.UserQuestionBookResponse;
import dnd.studyplanner.dto.questionbook.response.UserSolveQuestionResponse;

public interface IQuestionBookService {

	List<String> saveQuestionBook(String accessToken, QuestionBookDto saveDto);

	List<UserQuestionBookResponse> getAllUserQuestionBooks(String accessToken);

	boolean solveQuestionBook(String accessToken, QuestionBookSolveDto requestDto);

	List<UserSolveQuestion> getUserSolveDetails(String accessToken, Long questionBookId);

	public int getRecentQuestionBookCount(Long userId, Long goalId);
}
