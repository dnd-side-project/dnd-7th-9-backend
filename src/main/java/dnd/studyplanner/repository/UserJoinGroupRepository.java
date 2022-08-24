package dnd.studyplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.studyplanner.domain.user.model.UserJoinGroup;

public interface UserJoinGroupRepository extends JpaRepository<UserJoinGroup, Long> {

	List<UserJoinGroup> findByUserId(Long userId);
}
