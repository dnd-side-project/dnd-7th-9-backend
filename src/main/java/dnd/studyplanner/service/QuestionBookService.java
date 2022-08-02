package dnd.studyplanner.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.dto.option.request.OptionSaveDto;
import dnd.studyplanner.dto.question.request.QuestionListDto;
import dnd.studyplanner.dto.questionbook.request.QuestionBookDto;
import dnd.studyplanner.dto.questionbook.request.QuestionBookSaveDto;
import dnd.studyplanner.repository.QuestionBookRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class QuestionBookService {

	private final QuestionBookRepository questionBookRepository;

	private final QuestionService questionService;


	public void saveQuestionBook(QuestionBookDto saveDto) {
		saveDto.getGoalId();
		saveDto.getCreateUserId();

		// saveDto.toEntity(goal, user);
		// questionBookRepository.save(entity)
		List<QuestionListDto> questionDtoList = saveDto.getQuestionDtoList();

		List<Question> questions = new ArrayList<>();
		MultiValueMap<Question, OptionSaveDto> optionBuffer = new LinkedMultiValueMap<>();

		for (QuestionListDto questionListDto : questionDtoList) {

		}

	}
}
