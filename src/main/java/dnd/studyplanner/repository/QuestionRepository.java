package dnd.studyplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.studyplanner.domain.question.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

	List<Question> findByQuestionBookId(Long questionBookId);

}
