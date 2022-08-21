package dnd.studyplanner.service.Impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.option.model.Option;
import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.domain.user.model.UserJoinGroup;
import dnd.studyplanner.domain.user.model.UserSolveQuestion;
import dnd.studyplanner.domain.user.model.UserSolveQuestionBook;
import dnd.studyplanner.dto.option.request.OptionSaveDto;
import dnd.studyplanner.dto.question.request.QuestionListDto;
import dnd.studyplanner.dto.question.request.QuestionSolveDto;
import dnd.studyplanner.dto.questionbook.request.QuestionBookDto;
import dnd.studyplanner.dto.questionbook.request.QuestionBookSolveDto;
import dnd.studyplanner.dto.questionbook.response.UserQuestionBookResponse;
import dnd.studyplanner.dto.questionbook.response.UserSolveQuestionResponse;
import dnd.studyplanner.jwt.JwtService;
import dnd.studyplanner.repository.GoalRepository;
import dnd.studyplanner.repository.QuestionBookRepository;
import dnd.studyplanner.repository.QuestionRepository;
import dnd.studyplanner.repository.UserRepository;
import dnd.studyplanner.repository.UserSolveQuestionBookRepository;
import dnd.studyplanner.repository.UserSolveQuestionRepository;
import dnd.studyplanner.service.IOptionService;
import dnd.studyplanner.service.IQuestionBookService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class QuestionBookService implements IQuestionBookService {

	private final QuestionBookRepository questionBookRepository;

	private final IOptionService optionService;
	private final JwtService jwtService;

	private final UserRepository userRepository;
	private final GoalRepository goalRepository;
	private final QuestionRepository questionRepository;
	private final UserSolveQuestionRepository userSolveQuestionRepository;
	private final UserSolveQuestionBookRepository userSolveQuestionBookRepository;

	public List<String> saveQuestionBook(String accessToken, QuestionBookDto saveDto) {
		Long userId = jwtService.getUserId(accessToken);

		User user = userRepository.findById(userId).get();
		Goal goal = goalRepository.findById(saveDto.getGoalId()).get();

		QuestionBook entity = saveDto.toEntity(goal, user);
		QuestionBook questionBook = questionBookRepository.save(entity);

		List<String> questionContentList = new ArrayList<>();
		MultiValueMap<Question, OptionSaveDto> optionBuffer = new LinkedMultiValueMap<>();

		List<Option> options = new LinkedList<>();
		List<Question> questions = new LinkedList<>();

		for (QuestionListDto listDto : saveDto.getQuestionDtoList()) {
			Question question = listDto.toEntity(questionBook);
			questions.add(question);
			questionContentList.add(question.getQuestionContent());

			listDto.getOptionSaveDtoList()
				.forEach(o -> optionBuffer.add(question, o));
		}

		questionRepository.saveAll(questions); // 문제 List 저장

		for (Question question : optionBuffer.keySet()) {
			options.addAll(
				optionBuffer.get(question)
					.stream()
					.map(o -> o.toEntity(question))
					.collect(Collectors.toList())
			);

		}

		optionService.saveAllOptions(options);
		saveUserQuestionBook(goal, questionBook);

		return questionContentList;
	}

	@Override
	public List<UserQuestionBookResponse> getAllUserQuestionBooks(String accessToken) {
		Long userId = jwtService.getUserId(accessToken);
		List<UserQuestionBookResponse> response = new LinkedList<>();

		userSolveQuestionBookRepository.findAllBySolveUser_IdOrderByCreatedDateDesc(userId)
			.forEach(e -> response.add(
				UserQuestionBookResponse.builder()
					.user(e.getSolveUser())
					.questionBook(e.getSolveQuestionBook())
					.isSolved(e.isSolved())
					.build()
			));

		return response;
	}

	@Override
	public boolean solveQuestionBook(String accessToken, QuestionBookSolveDto requestDto) {
		Long userId = jwtService.getUserId(accessToken);
		Optional<User> user = userRepository.findById(userId);
		Long questionBookId = requestDto.getQuestionBookId();

		// DB 커넥션을 줄이기 위해 Map 자료구조 사용
		Map<Long, Question> questionMap = questionRepository.findByQuestionBookId(questionBookId).stream()
			.collect(Collectors.toMap(Question::getId, v -> v));
		List<QuestionSolveDto> questionSolveDto = requestDto.getSolveDtoList();


		List<UserSolveQuestion> userSolveQuestions = new ArrayList<>(); //풀이 정보 saveAll 하기 위한 리스트
		int answerCount = 0;

		for (QuestionSolveDto solveDto : questionSolveDto) {
			UserSolveQuestion userSolveQuestion = solveDto.toEntity(
				user.get(),
				questionMap.get(solveDto.getQuestionId())); // Map을 통해 조회하여 DB에 직접 연결하는 횟수를 줄임

			if (userSolveQuestion.isRightCheck()) {
				answerCount += 1;
			}

			userSolveQuestions.add(userSolveQuestion);
		}

		userSolveQuestionRepository.saveAll(userSolveQuestions);

		return isPassedQuestionBook(userId, questionBookId, answerCount);
	}

	@Override
	public List<UserSolveQuestion> getUserSolveDetails(String accessToken, Long questionBookId) {
		Long userId = jwtService.getUserId(accessToken);
		return userSolveQuestionRepository.findBySolveUserIdAndSolveQuestionBookId(userId, questionBookId);
	}

	private boolean isPassedQuestionBook(Long userId, Long questionBookId, int answerCount) {
		Optional<QuestionBook> questionBook = questionBookRepository.findById(questionBookId);
		// Question Book이 포함된 목표의 최소 정답률과 비교
		Goal goal = questionBook.get().getQuestionBookGoal();

		boolean isPass = false;
		if (answerCount >= goal.getMinAnswerPerQuestionBook()) {
			isPass = true;
		}

		UserSolveQuestionBook userSolveQuestionBook = userSolveQuestionBookRepository.findBySolveUserIdAndSolveQuestionBookId(
			userId, questionBookId).get();

		userSolveQuestionBook.updateAfterSolve(isPass, answerCount);
		return isPass;
	}

	@Override
	public int getRecentQuestionBookCount(Long userId, Long goalId) {
		// 조회하는 세부목표에 가장 최근에 추가된 문제집
		QuestionBook recentQuestionBook = questionBookRepository
			.findByQuestionBookGoal_IdOrderByCreatedDateDesc(goalId)
			.get();

		UserSolveQuestionBook userSolveQuestionBook = userSolveQuestionBookRepository
			.findBySolveUserIdAndSolveQuestionBookId(userId, recentQuestionBook.getId())
			.get();

		// 가장 최근에 추가된 문제집을 풀었으면 return 0
		if (userSolveQuestionBook.isSolved()) {
			return 0;
		}

		// 아직 풀지 않았다면, 문제집 개수 반환 -> 추가된(전체에 대한) 문제집 수
		return recentQuestionBook.getQuestionBookQuestionNum();
	}

	/**
	 * 풀어야할 User와 새로 생성된 QuestionBook 의 관계 저장
	 * @param goal
	 * @param questionBook
	 */
	private void saveUserQuestionBook(Goal goal, QuestionBook questionBook) {
		StudyGroup studyGroup = goal.getStudyGroup(); //현재 세부 목표를 포함하는 스터디 그룹

		List<UserSolveQuestionBook> userSolveQuestionBooks = studyGroup.getUserJoinGroups().stream()
			.map(UserJoinGroup::getUser) // 스터디 그룹원들을 조회
			.map(solveUser -> UserSolveQuestionBook.builder() // 스터디원 - 문제집 저장
				.solveUser(solveUser)
				.solveQuestionBook(questionBook)
				.questionNumber(questionBook.getQuestionBookQuestionNum())
				.build())
			.collect(Collectors.toList());
		userSolveQuestionBookRepository.saveAll(userSolveQuestionBooks);
	}
}
