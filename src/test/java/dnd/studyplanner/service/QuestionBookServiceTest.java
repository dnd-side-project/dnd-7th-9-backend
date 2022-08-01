package dnd.studyplanner.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
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
import dnd.studyplanner.dto.option.request.OptionSaveDto;
import dnd.studyplanner.dto.question.request.QuestionListDto;
import dnd.studyplanner.repository.OptionRepository;
import dnd.studyplanner.repository.QuestionRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class QuestionBookServiceTest {

	@Autowired
	DataUtil dataUtil;

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	OptionRepository optionRepository;

	@Test
	public void saveQuestionsTest() {
	    //given
		QuestionBook questionBook = dataUtil.saveQuestionBookData();
		List<QuestionListDto> questionListDto = dataUtil.getQuestionListDto();

		//when
		List<Question> questions = new ArrayList<>();

		// MutiValueMap으로 Question-Options 저장
		MultiValueMap<Question, OptionSaveDto> optionBuffer = new LinkedMultiValueMap<>();

		for (QuestionListDto listDto : questionListDto) { // 여러 문제(Question)을 순회하면서
			Question entity = listDto.toEntity(questionBook);

			questions.add(entity);

			// 하나의 Question에 저장되어있는 Option들을 MutiValueMap에 저장
			List<OptionSaveDto> optionSaveDtoList = listDto.getOptionSaveDtoList();
			for (OptionSaveDto optionSaveDto : optionSaveDtoList) {
				optionBuffer.add(entity, optionSaveDto);
			}
		}

		questionRepository.saveAll(questions); // 문제 List 저장

		List<Option> options = new ArrayList<>(); // saveAll 할 option 리스트 초기화

		// MutivalueMap에 저장되어있던 객체(Question)를 통해 DB조회 없이 관계를 찾을 수 있음
		for (Question question : optionBuffer.keySet()) {
			for (OptionSaveDto optionSaveDto : optionBuffer.get(question)) {
				options.add(
					optionSaveDto.toEntity(question)
				);
			}
		} // N * M 연산이 이루어짐...

		optionRepository.saveAll(options);

	    //then
		List<Question> allQuestions = questionRepository.findAll();
		List<Option> allOptions = optionRepository.findAll();

		assertThat(allQuestions.size()).isEqualTo(2);

		// Test Question A : 3, B : 4개의 Option을 가짐
		assertThat(allOptions.size()).isEqualTo(7);
	}

}