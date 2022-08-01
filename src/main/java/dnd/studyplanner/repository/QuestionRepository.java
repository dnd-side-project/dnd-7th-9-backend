package dnd.studyplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.studyplanner.domain.question.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
