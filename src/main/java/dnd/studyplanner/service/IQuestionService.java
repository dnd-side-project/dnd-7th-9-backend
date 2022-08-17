package dnd.studyplanner.service;

import java.util.List;

import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.dto.question.request.QuestionSaveDto;
import dnd.studyplanner.dto.question.request.QuestionSolveDto;
import dnd.studyplanner.dto.question.response.QuestionResponseDto;
import dnd.studyplanner.exception.BaseException;

public interface IQuestionService {

	Question saveQuestion(QuestionSaveDto saveDto) throws BaseException;
	void saveAllQuestions(List<Question> questionList);

	List<QuestionResponseDto> getQuestions(Long questionBookId);
	boolean solveQuestion(QuestionSolveDto solveDto, String accessToken) throws BaseException;
}
