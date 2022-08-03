package dnd.studyplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.studyplanner.domain.questionbook.model.QuestionBook;

public interface QuestionBookRepository extends JpaRepository<QuestionBook, Long> {
}
