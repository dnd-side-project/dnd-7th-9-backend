package dnd.studyplanner.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dnd.studyplanner.domain.option.model.Option;
import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.dto.option.request.OptionListSaveDto;
import dnd.studyplanner.dto.option.request.OptionSaveDto;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.repository.OptionRepository;
import dnd.studyplanner.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class OptionService {

	private final QuestionRepository questionRepository;
	private final OptionRepository optionRepository;

	public void saveOptions(OptionListSaveDto saveDto) throws BaseException {
		Question question = questionRepository.findById(saveDto.getQuestionId())
			.orElseThrow(BaseException::new);

		List<Option> options = new ArrayList<>();
		for (OptionSaveDto optionDto : saveDto.getOptions()) {
			options.add(optionDto.toEntity(question));
		}

		optionRepository.saveAll(options);
	}


}
