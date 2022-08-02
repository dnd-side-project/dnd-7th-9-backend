package dnd.studyplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.studyplanner.domain.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
