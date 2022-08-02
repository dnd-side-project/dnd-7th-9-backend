package dnd.studyplanner.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.dto.question.request.QuestionSaveDto;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.repository.QuestionBookRepository;
import dnd.studyplanner.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class QuestionService {

	private final QuestionRepository questionRepository;
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
}
