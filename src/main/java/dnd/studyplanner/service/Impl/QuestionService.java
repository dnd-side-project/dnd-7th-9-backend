package dnd.studyplanner.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.domain.user.model.UserSolveQuestion;
import dnd.studyplanner.dto.question.request.QuestionSaveDto;
import dnd.studyplanner.dto.question.request.QuestionSolveDto;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.jwt.JwtService;
import dnd.studyplanner.repository.QuestionBookRepository;
import dnd.studyplanner.repository.QuestionRepository;
import dnd.studyplanner.repository.UserRepository;
import dnd.studyplanner.repository.UserSolveQuestionRepository;
import dnd.studyplanner.service.IQuestionService;
import dnd.studyplanner.service.IUserService;
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
