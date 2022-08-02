package dnd.studyplanner.service;

import java.util.List;

import dnd.studyplanner.domain.question.model.Question;

public interface IQuestionService {
	void saveAllQuestions(List<Question> questionList);
}
