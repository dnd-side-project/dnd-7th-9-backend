package dnd.studyplanner.service;

import java.util.List;

import dnd.studyplanner.domain.option.model.Option;

public interface IOptionService {
	void saveAllOptions(List<Option> options);
}
