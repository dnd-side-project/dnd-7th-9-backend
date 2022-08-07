package dnd.studyplanner.repository;

import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
}
