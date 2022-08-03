package dnd.studyplanner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.question.model.QuestionOptionType;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.dto.option.request.OptionSaveDto;
import dnd.studyplanner.dto.question.request.QuestionListDto;
import dnd.studyplanner.dto.questionbook.request.QuestionBookDto;
import dnd.studyplanner.repository.QuestionBookRepository;
import dnd.studyplanner.repository.QuestionRepository;

@Component
public class DataUtil {

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	QuestionBookRepository questionBookRepository;

	public Question saveQuestionData() {
		QuestionBook questionBook = questionBookRepository.save(
			QuestionBook.builder().build()
		);

		Question question = questionRepository.save(
			Question.builder()
				.questionBook(questionBook)
				.build()
		);

		return question;
	}

	public QuestionBook saveQuestionBookData() {
		QuestionBook questionBook = questionBookRepository.save(
			QuestionBook.builder().build()
		);

		return questionBook;
	}

	public List<QuestionListDto> getQuestionListDto() {

		List<QuestionListDto> requestDto = new ArrayList<>();

		List<OptionSaveDto> optionsA = new ArrayList<>();
		for (int i = 1; i < 4; i++) {
			optionsA.add(OptionSaveDto.builder()
				.optionContent("Test OptionA : " + i)
				.optionImageEnable(false)
				.optionImageUrl("").build());
		}

		List<OptionSaveDto> optionsB = new ArrayList<>();
		for (int i = 1; i < 5; i++) {
			optionsB.add(OptionSaveDto.builder()
				.optionContent("Test OptionB : " + i)
				.optionImageEnable(true)
				.optionImageUrl("testImg.com/" + i).build());
		}

		List<OptionSaveDto> optionsC = new ArrayList<>();
		for (int i = 1; i < 2; i++) {
			optionsB.add(OptionSaveDto.builder()
				.optionContent("Test OptionB : " + i)
				.optionImageEnable(true)
				.optionImageUrl("testImg.com/" + i).build());
		}

		List<OptionSaveDto> optionsD = new ArrayList<>();
		for (int i = 1; i < 3; i++) {
			optionsB.add(OptionSaveDto.builder()
				.optionContent("Test OptionB : " + i)
				.optionImageEnable(true)
				.optionImageUrl("testImg.com/" + i).build());
		}

		requestDto.add(
			QuestionListDto.builder()
				.questionContent("Test Question A")
				.questionOptionType(QuestionOptionType.TEXT)
				.questionAnswer(3)
				.optionSaveDtoList(optionsA)
				.build()
		);

		requestDto.add(
			QuestionListDto.builder()
				.questionContent("Test Question B")
				.questionOptionType(QuestionOptionType.IMAGE)
				.questionAnswer(3)
				.optionSaveDtoList(optionsB)
				.build()
		);

		requestDto.add(
			QuestionListDto.builder()
				.questionContent("Test Question C")
				.questionOptionType(QuestionOptionType.IMAGE)
				.questionAnswer(5)
				.optionSaveDtoList(optionsC)
				.build()
		);

		requestDto.add(
			QuestionListDto.builder()
				.questionContent("Test Question D")
				.questionOptionType(QuestionOptionType.IMAGE)
				.questionAnswer(1)
				.optionSaveDtoList(optionsD)
				.build()
		);

		return requestDto;
	}

	public QuestionBookDto getQuestionBookDto() {
		List<QuestionListDto> questionListDto = getQuestionListDto();

		return QuestionBookDto.builder()
				.goalId(1L)
				.createUserId(1L)
				.questionBookName("고양이 문제")
				.questionBookQuestionNum(3)
				.questionBookMinAchieveRate(80)
				.questionDtoList(questionListDto)
				.build();
	}
}
