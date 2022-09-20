package dnd.studyplanner.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.studyplanner.domain.user.model.UserSolveQuestionBook;

public interface UserSolveQuestionBookRepository extends JpaRepository<UserSolveQuestionBook, Long> {

	// 사용자가 풀어야 하는 문제집 리스트 조회 - 최근 등록일 순 ?
	List<UserSolveQuestionBook> findAllBySolveUser_IdOrderByCreatedDateDesc(Long id);

	Optional<UserSolveQuestionBook> findBySolveUserIdAndSolveQuestionBookId(Long userId, Long questionBookId);

	List<UserSolveQuestionBook> findBySolveQuestionBookId(Long questionBookId);
}
