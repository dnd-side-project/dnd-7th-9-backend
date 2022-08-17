package dnd.studyplanner.service;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import dnd.studyplanner.DataUtil;
import dnd.studyplanner.domain.option.model.Option;
import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.domain.user.model.UserSolveQuestion;
import dnd.studyplanner.dto.option.request.OptionSaveDto;
import dnd.studyplanner.dto.question.request.QuestionListDto;
import dnd.studyplanner.dto.question.request.QuestionSolveDto;
import dnd.studyplanner.dto.questionbook.request.QuestionBookDto;
import dnd.studyplanner.dto.questionbook.request.QuestionBookSolveDto;
import dnd.studyplanner.repository.OptionRepository;
import dnd.studyplanner.repository.QuestionRepository;
import dnd.studyplanner.repository.UserRepository;
import dnd.studyplanner.repository.UserSolveQuestionRepository;
import dnd.studyplanner.service.Impl.QuestionBookService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class QuestionBookServiceTest {

	@Autowired
	DataUtil dataUtil;

	@Autowired
	QuestionBookService questionBookService;
	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	OptionRepository optionRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserSolveQuestionRepository userSolveQuestionRepository;

	@Test
	public void saveQuestionsTest() {
		long a = System.currentTimeMillis();
		//given
		QuestionBook questionBook = dataUtil.saveQuestionBookData();
		List<QuestionListDto> questionListDto = dataUtil.getQuestionListDto();

		//when
		List<Option> options = new LinkedList<>();
		MultiValueMap<Question, OptionSaveDto> optionBuffer = new LinkedMultiValueMap<>();

		List<Question> questions = new LinkedList<>();

		for (QuestionListDto listDto : questionListDto) {
			Question entity = listDto.toEntity(questionBook);
			questions.add(entity);

			listDto.getOptionSaveDtoList()
				.forEach(o -> optionBuffer.add(entity, o));
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

		optionRepository.saveAll(options);

		//then
		List<Question> allQuestions = questionRepository.findAll();
		List<Option> allOptions = optionRepository.findAll();

		assertThat(allQuestions.size()).isEqualTo(4);
		// assertThat(allOptions.size()).isEqualTo(82);

		System.out.println("-----------------------------------");
		System.out.println(System.currentTimeMillis() - a);
		System.out.println("-----------------------------------");
	}

	@DisplayName(value = "Service 로직 사용")
	@Test
	void saveAsListTest() {
		//given
		QuestionBookDto questionBookDto = dataUtil.getQuestionBookDto();

		//when
		questionBookService.saveQuestionBook("accessTokne", questionBookDto);

		List<Question> allQuestions = questionRepository.findAll();
		List<Option> allOptions = optionRepository.findAll();

		//then
		assertThat(allQuestions.size()).isEqualTo(4);
		assertThat(allOptions.size()).isEqualTo(10);
	}


	@DisplayName(value = "문제집 풀이 완료 테스트")
	@Transactional
	@Test
	void solveQuestion() {
	    //given
		List<QuestionSolveDto> questionSolveList = new ArrayList<>();

		questionSolveList.add(QuestionSolveDto.builder().questionId(13L).checkAnswer(3).build());
		questionSolveList.add(QuestionSolveDto.builder().questionId(14L).checkAnswer(1).build());
		questionSolveList.add(QuestionSolveDto.builder().questionId(15L).checkAnswer(3).build());

		QuestionBookSolveDto requestDto = QuestionBookSolveDto.builder().
			questionBookId(5L).solveDtoList(questionSolveList).build();

		//when
		boolean passQuestionBook = questionBookService.solveQuestionBook(
			"accessToken", // 임시 입력
			requestDto);

		List<UserSolveQuestion> allBySolveUserId = userSolveQuestionRepository.findAllBySolveUserId(1L);
		//then
		assertThat(passQuestionBook).isTrue();
		assertThat(allBySolveUserId.size()).isEqualTo(3);
	}

}