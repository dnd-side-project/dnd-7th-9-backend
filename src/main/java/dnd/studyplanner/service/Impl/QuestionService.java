package dnd.studyplanner.service.Impl;

import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.domain.user.model.UserSolveQuestion;
import dnd.studyplanner.dto.question.request.QuestionSaveDto;
import dnd.studyplanner.dto.question.request.QuestionSolveDto;
import dnd.studyplanner.dto.question.response.QuestionListResponseDto;
import dnd.studyplanner.dto.question.response.QuestionResponseDto;
import dnd.studyplanner.dto.response.CustomResponseStatus;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.jwt.JwtService;
import dnd.studyplanner.repository.QuestionBookRepository;
import dnd.studyplanner.repository.QuestionRepository;
import dnd.studyplanner.repository.UserRepository;
import dnd.studyplanner.repository.UserSolveQuestionRepository;
import dnd.studyplanner.service.IQuestionService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class QuestionService implements IQuestionService {

	private final QuestionRepository questionRepository;

	private final JwtService jwtService;
	private final UserRepository userRepository;

	private final UserSolveQuestionRepository userSolveQuestionRepository;
	private final QuestionBookRepository questionBookRepository;

	public Question saveQuestion(QuestionSaveDto saveDto) throws BaseException {
		QuestionBook questionBook = questionBookRepository.findById(saveDto.getQuestionBookId())
			.orElseThrow(BaseException::new);
		Question question = saveDto.toEntity(questionBook);
		return questionRepository.save(question);
	}

	public void saveAllQuestions(List<Question> questionList) {
		questionRepository.saveAll(questionList);
	}

	@Override
	public QuestionListResponseDto getQuestions(Long questionBookId) throws BaseException {
		List<Question> questions = questionRepository.findByQuestionBookId(questionBookId);
		List<QuestionResponseDto> questionList = questions.stream()
			.map(Question::toResponseDto)
			.collect(Collectors.toList());

		if (questions.isEmpty()) {
			throw new BaseException(NOT_EXIST_DATA);
		}

		QuestionBook questionBook = questions.get(0).getQuestionBook();
		Goal goal = questionBook.getQuestionBookGoal();

		return QuestionListResponseDto.builder()
			.goalContent(goal.getGoalContent())
			.questionBookContent(questionBook.getQuestionBookName())
			.questionList(questionList)
			.build();
	}

	@Override
	public boolean solveQuestion(QuestionSolveDto solveDto, String accessToken) throws BaseException {
		Long userId = jwtService.getUserId(accessToken);

		Long questionId = solveDto.getQuestionId();
		User user = userRepository.findById(userId)
			.orElseThrow(BaseException::new);
		Question question = questionRepository.findById(questionId)
			.orElseThrow(BaseException::new);

		UserSolveQuestion userSolveQuestion = solveDto.toEntity(user, question);
		userSolveQuestionRepository.save(userSolveQuestion);

		return userSolveQuestion.isRightCheck();
	}
}
