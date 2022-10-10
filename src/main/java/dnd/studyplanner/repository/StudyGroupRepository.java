package dnd.studyplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dnd.studyplanner.domain.studygroup.model.StudyGroup;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
}
