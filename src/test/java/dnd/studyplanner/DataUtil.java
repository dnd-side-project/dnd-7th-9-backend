package dnd.studyplanner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
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
}
