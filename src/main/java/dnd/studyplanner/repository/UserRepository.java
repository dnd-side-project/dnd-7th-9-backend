package dnd.studyplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.studyplanner.domain.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long userId);

    Optional<User> findByUserEmail(String userEmail);

    List<User> findByUserEmailContaining(String userEmail);

}
