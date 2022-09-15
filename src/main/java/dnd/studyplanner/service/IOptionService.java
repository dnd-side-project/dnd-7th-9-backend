package dnd.studyplanner.service;

import java.util.List;

import dnd.studyplanner.domain.option.model.Option;
import dnd.studyplanner.domain.question.model.Question;

public interface IOptionService {
	void saveAllOptions(List<Option> options);

	List<Option> findByAllByQuestionList(List<Question> questionList);
}
