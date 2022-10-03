package dnd.studyplanner.service.Impl;

import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import dnd.studyplanner.domain.user.model.UserCheckOption;
import dnd.studyplanner.domain.user.model.UserJoinGroup;
import dnd.studyplanner.domain.user.model.UserSolveQuestion;
import dnd.studyplanner.domain.user.model.UserSolveQuestionBook;
import dnd.studyplanner.dto.option.request.OptionSaveDto;
import dnd.studyplanner.dto.option.response.OptionSolvedDetailResponseDto;
import dnd.studyplanner.dto.question.request.QuestionListDto;
import dnd.studyplanner.dto.question.request.QuestionSolveDto;
import dnd.studyplanner.dto.questionbook.request.QuestionBookDto;
import dnd.studyplanner.dto.questionbook.request.QuestionBookSolveDto;
import dnd.studyplanner.dto.questionbook.response.UserQuestionBookResponse;
import dnd.studyplanner.dto.questionbook.response.UserSolveQuestionResponse;
import dnd.studyplanner.exception.BaseException;
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

	public List<String> saveQuestionBook(String accessToken, QuestionBookDto saveDto) throws BaseException {
		Long userId = jwtService.getUserId(accessToken);

		User user = userRepository.findById(userId).get();
		Goal goal = goalRepository.findById(saveDto.getGoalId()).get();

		QuestionBook entity = saveDto.toEntity(goal, user);
		QuestionBook questionBook = questionBookRepository.save(entity);

		List<String> questionContentList = new ArrayList<>();
		MultiValueMap<Question, OptionSaveDto> optionBuffer = new LinkedMultiValueMap<>();

		List<Option> options = new LinkedList<>();
		List<Question> questions = new LinkedList<>();

		if (goal.getQuestionPerQuestionBook() != (saveDto.getQuestionDtoList().size())) {
			throw new BaseException(QUESTION_AMOUNT_UNMATCHED);
		}

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
	public boolean solveQuestionBook(String accessToken, QuestionBookSolveDto requestDto) throws BaseException {
		Long userId = jwtService.getUserId(accessToken);
		User user = userRepository.findById(userId).orElseThrow(
			() -> new BaseException(NOT_EXIST_USER)
		);

		Long questionBookId = requestDto.getQuestionBookId();

		List<Question> questionList = questionRepository.findByQuestionBookId(questionBookId);

		// DB 커넥션을 줄이기 위해 Map 자료구조 사용
		Map<Long, Question> questionMap = questionList.stream()
			.collect(Collectors.toMap(Question::getId, v -> v));

		Map<Long, Option> optionMap = optionService.findByAllByQuestionList(questionList).stream()
			.collect(Collectors.toMap(Option::getId, v -> v));

		List<QuestionSolveDto> questionSolveDto = requestDto.getSolveDtoList();


		List<UserSolveQuestion> userSolveQuestions = new ArrayList<>(); //풀이 정보 saveAll 하기 위한 리스트
		int answerCount = 0;

		for (QuestionSolveDto solveDto : questionSolveDto) {

			UserSolveQuestion userSolveQuestion = solveDto.toEntity(user, questionMap.get(solveDto.getQuestionId()));

			for (Long checkOptionId : solveDto.getCheckOptionIdList()) {
				//유저가 선택한 옵션 저장
				UserCheckOption.builder()
					.userSolveQuestion(userSolveQuestion)
					.option(optionMap.get(checkOptionId))
					.build();
			}

			if (isCorrect(userSolveQuestion)) {
				userSolveQuestion.setRightCheck(true); // 정답 반영
				answerCount += 1;
			}

			userSolveQuestions.add(userSolveQuestion);
		}

		userSolveQuestionRepository.saveAll(userSolveQuestions);

		return isPassedQuestionBook(userId, questionBookId, answerCount);
	}

	@Override
	public List<UserSolveQuestionResponse> getUserSolveDetails(String accessToken, Long questionBookId) {
		Long userId = jwtService.getUserId(accessToken);
		List<UserSolveQuestion> userSolvedQuestions = userSolveQuestionRepository.findBySolveUserIdAndSolveQuestionBookId(
			userId, questionBookId);

		List<UserSolveQuestionResponse> userSolveQuestionDetails = new ArrayList<>();

		for (UserSolveQuestion userSolvedQuestion : userSolvedQuestions) {
			Question question = userSolvedQuestion.getSolveQuestion();

			userSolveQuestionDetails.add(
				UserSolveQuestionResponse.builder()
					.questionId(question.getId())
					.questionContent(question.getQuestionContent())
					.questionImage(question.getQuestionImage())
					.rightCheck(userSolvedQuestion.isRightCheck())
					.optionList(getOptionSolveInfos(userSolvedQuestion))
					.build());
		}

		return userSolveQuestionDetails;
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

	@Override
	public boolean isSolvedQuestionBook(String accessToken, Long questionBookId) throws BaseException {
		Long userId = jwtService.getUserId(accessToken);
		UserSolveQuestionBook userSolveQuestionBook = userSolveQuestionBookRepository.findBySolveUserIdAndSolveQuestionBookId(
			userId, questionBookId).orElseThrow(() -> new BaseException(UNAUTHORIZED_QUESTION_BOOK));

		return userSolveQuestionBook.isSolved();
	}

	@Override
	public void editQuestionBook(String accessToken, Long questionBookId, QuestionBookDto saveDto) throws BaseException {
		Long userId = jwtService.getUserId(accessToken);
		QuestionBook questionBook = questionBookRepository.findById(questionBookId)
			.orElseThrow(() -> new BaseException(NOT_EXIST_DATA));

		if (!questionBook.getQuestionBookCreateUser().getId().equals(userId)) {
			throw new BaseException(UNAUTHORIZED_REQUEST);
		} else if (questionBook.getQuestionBookQuestionNum() != (saveDto.getQuestionDtoList().size())) {
			throw new BaseException(QUESTION_AMOUNT_UNMATCHED);
		}

		questionBook.updateByEditDto(saveDto);

		List<Question> questions = questionBook.getQuestions();
		List<QuestionListDto> questionDtoList = saveDto.getQuestionDtoList();

		// 풀이 정보가 변경되었는지 체크
		if (userSolveQuestionBookRepository.existsBySolveQuestionBookIdAndIsSolved(questionBookId, true)) {
			throw new BaseException(QUESTION_BOOK_ALREADY_SOLVED);
		}

		// 기존 문제의 개수와 수정 dto내 문제 개수가 동일함이 보장되어 있음
		for (int i = 0; i < questions.size(); i++) {
			Question question = questions.get(i);
			QuestionListDto editQuestionDto = questionDtoList.get(i);
			question.updateByEditDto(editQuestionDto);

			// option의 개수는 달라 질 수 있어서 remove -> create로 동작
			question.clearOptions();
			optionService.deleteByQuestion(question);
			for (OptionSaveDto optionSaveDto : editQuestionDto.getOptionSaveDtoList()) {
				optionSaveDto.toEntity(question);
			}

		}

		questionBookRepository.save(questionBook);
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

	private boolean isCorrect(UserSolveQuestion userSolveQuestion) {
		List<UserCheckOption> userCheckOptions = userSolveQuestion.getUserCheckOptions();
		int answerCount = userSolveQuestion.getSolveQuestion().getAnswerCount();
		int correctCount = 0;
		for (UserCheckOption userCheckOption : userCheckOptions) {
			if (userCheckOption.getOption().isAnswer()) {
				correctCount += 1; //사용자 체크 옵션 중 정답 개수 count
			}
		}

		return answerCount == correctCount; //문제의 정답 개수와 사용자가 맞춘 개수가 같으면 return true
	}

	/**
	 * 풀어야할 User와 새로 생성된 QuestionBook 의 관계 저장
	 * @param goal
	 * @param questionBook
	 */
	private void saveUserQuestionBook(Goal goal, QuestionBook questionBook) {
		StudyGroup studyGroup = goal.getStudyGroup(); //현재 세부 목표를 포함하는 스터디 그룹

		Long questionBookUserId = questionBook.getQuestionBookCreateUser().getId();

		List<UserSolveQuestionBook> userSolveQuestionBooks = studyGroup.getUserJoinGroups().stream()
			.map(UserJoinGroup::getUser) // 스터디 그룹원들을 조회
			.filter(o -> !o.getId().equals(questionBookUserId)) // 본인이 출제한 문제는 풀 필요 X
			.map(solveUser -> UserSolveQuestionBook.builder() // 스터디원 - 문제집 저장
				.solveUser(solveUser)
				.solveQuestionBook(questionBook)
				.questionNumber(questionBook.getQuestionBookQuestionNum())
				.build())
			.collect(Collectors.toList());
		userSolveQuestionBookRepository.saveAll(userSolveQuestionBooks);
	}

	private List<OptionSolvedDetailResponseDto> getOptionSolveInfos(UserSolveQuestion userSolveQuestion) {
		List<OptionSolvedDetailResponseDto> optionSolveInfoList = new ArrayList<>();

		Set<Option> userCheckOptionSet = userSolveQuestion.getUserCheckOptions().stream()
			.map(UserCheckOption::getOption)
			.collect(Collectors.toSet());

		List<Option> options = userSolveQuestion.getSolveQuestion().getOptions();

		for (Option option : options) {
			optionSolveInfoList.add(
				OptionSolvedDetailResponseDto.builder()
					.option(option)
					.isChecked(userCheckOptionSet.contains(option))
					.build());
		}

		return optionSolveInfoList;
	}
}
