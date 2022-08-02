package dnd.studyplanner.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import dnd.studyplanner.DataUtil;
import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.question.model.QuestionOptionType;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.dto.question.request.QuestionSaveDto;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.repository.QuestionBookRepository;
import dnd.studyplanner.repository.QuestionRepository;
import dnd.studyplanner.service.Impl.QuestionService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class QuestionServiceTest {

	@Autowired
	QuestionService questionService;

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	QuestionBookRepository questionBookRepository;

	@Autowired
	DataUtil dataUtil;

	@Transactional
	@DisplayName(value = "문제 저장 테스트")
	@Test
	public void saveQuestionTest() throws BaseException {
		//given
		QuestionBook questionBook = dataUtil.saveQuestionBookData();

		QuestionSaveDto saveDto = QuestionSaveDto.builder()
			.questionAnswer(3)
			.questionContent("고양이는 귀엽나요?")
			.questionOptionType(QuestionOptionType.IMAGE)
			.questionBookId(questionBook.getId())
			.build();

		System.out.println("questionBook = " + questionBook.getId());
		//when
		Question saveQuestion = questionService.saveQuestion(saveDto);
		//then
		assertThat(saveQuestion.getQuestionBook().getId()).isEqualTo(questionBook.getId());
		assertThat(questionBook.getQuestions().get(0).getQuestionContent())
			.isEqualTo("고양이는 귀엽나요?");
	}

	@Transactional
	@DisplayName(value = "문제집 예외 테스트")
	@Test
	public void saveAtNoQuestionBookTest() throws BaseException {
		//given
		QuestionSaveDto saveDto = QuestionSaveDto.builder()
			.questionAnswer(3)
			.questionContent("고양이는 귀엽나요?")
			.questionOptionType(QuestionOptionType.IMAGE)
			.questionBookId(-1L) // 존재할 수 없는 문제집 Id
			.build();

		//then
		assertThatThrownBy(() -> {
			questionService.saveQuestion(saveDto);
		}).isInstanceOf(BaseException.class);
	}

}