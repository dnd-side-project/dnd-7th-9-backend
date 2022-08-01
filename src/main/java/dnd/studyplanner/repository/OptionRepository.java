package dnd.studyplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.studyplanner.domain.option.model.Option;

public interface OptionRepository extends JpaRepository<Option, Long> {

}
