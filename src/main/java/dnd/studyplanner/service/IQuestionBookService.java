package dnd.studyplanner.service;

import java.util.List;

import dnd.studyplanner.domain.user.model.UserSolveQuestion;
import dnd.studyplanner.dto.questionbook.request.QuestionBookDto;
import dnd.studyplanner.dto.questionbook.request.QuestionBookSolveDto;
import dnd.studyplanner.dto.questionbook.response.UserQuestionBookResponse;
import dnd.studyplanner.dto.questionbook.response.UserSolveQuestionResponse;
import dnd.studyplanner.exception.BaseException;

public interface IQuestionBookService {

	List<String> saveQuestionBook(String accessToken, QuestionBookDto saveDto) throws BaseException;

	List<UserQuestionBookResponse> getAllUserQuestionBooks(String accessToken);

	boolean solveQuestionBook(String accessToken, QuestionBookSolveDto requestDto) throws BaseException;

	List<UserSolveQuestionResponse> getUserSolveDetails(String accessToken, Long questionBookId);

	public int getRecentQuestionBookCount(Long userId, Long goalId);

	boolean isSolvedQuestionBook(String accessToken, Long questionBookId) throws BaseException;

	void editQuestionBook(String accessToken, Long questionBookId, QuestionBookDto saveDto) throws BaseException;
}
