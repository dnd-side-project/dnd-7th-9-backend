package dnd.studyplanner.service.Impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
import dnd.studyplanner.domain.user.model.UserSolveQuestionBook;
import dnd.studyplanner.dto.option.request.OptionSaveDto;
import dnd.studyplanner.dto.question.request.QuestionListDto;
import dnd.studyplanner.dto.questionbook.request.QuestionBookDto;
import dnd.studyplanner.repository.GoalRepository;
import dnd.studyplanner.repository.QuestionBookRepository;
import dnd.studyplanner.repository.UserRepository;
import dnd.studyplanner.repository.UserSolveQuestionBookRepository;
import dnd.studyplanner.service.IOptionService;
import dnd.studyplanner.service.IQuestionBookService;
import dnd.studyplanner.service.IQuestionService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class QuestionBookService implements IQuestionBookService {

	private final QuestionBookRepository questionBookRepository;

	private final IQuestionService questionService;

	private final IOptionService optionService;

	private final UserRepository userRepository;
	private final GoalRepository goalRepository;
	private final UserSolveQuestionBookRepository userSolveQuestionBookRepository;

	public List<String> saveQuestionBook(QuestionBookDto saveDto) {
		List<String> questionContentList = new ArrayList<>();

		// for Test
		// ID가 1인 entity 고정
		User user = userRepository.save(new User());
		Goal goal = goalRepository.save(new Goal());
		// 추후 User, Goal Service에 의존하여 Id에 해당하는 Entity를 가져와야함

		QuestionBook entity = saveDto.toEntity(goal, user);
		QuestionBook questionBook = questionBookRepository.save(entity);

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

		questionService.saveAllQuestions(questions); // 문제 List 저장

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
