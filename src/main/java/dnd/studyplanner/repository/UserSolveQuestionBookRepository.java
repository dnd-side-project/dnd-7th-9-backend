package dnd.studyplanner.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.studyplanner.domain.user.model.UserSolveQuestionBook;

public interface UserSolveQuestionBookRepository extends JpaRepository<UserSolveQuestionBook, Long> {
	List<UserSolveQuestionBook> findAllBySolveUser_IdOrderByCreatedDateDesc(Long id);

	Optional<UserSolveQuestionBook> findByUser_IdAndQuestionBook_Id(Long userId, Long questionBookId);
}