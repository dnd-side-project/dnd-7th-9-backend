package dnd.studyplanner.service;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import dnd.studyplanner.DataUtil;
import dnd.studyplanner.domain.option.model.Option;
import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.dto.option.request.OptionListSaveDto;
import dnd.studyplanner.dto.option.request.OptionSaveDto;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.repository.OptionRepository;
import dnd.studyplanner.repository.QuestionRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class OptionServiceTest {

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	OptionRepository optionRepository;

	@Autowired
	OptionService optionService;

	@Autowired
	DataUtil dataUtil;

	@Transactional
	@DisplayName(value = "Option 리스트로 저장")
	@Test
	public void optionSaveAsListTest() throws BaseException {
		//given
		Question question = dataUtil.saveQuestionData();
		int optionsCnt = 4;

		List<OptionSaveDto> optionSaveDtoList = new ArrayList<>();
		for (int i = 0; i < optionsCnt; i++) {
			optionSaveDtoList.add(
				OptionSaveDto.builder()
					.optionContent("Test Content" + i)
					.optionImageEnable(false)
					.optionImageUrl("")
					.build()
			);
		}

		OptionListSaveDto optionListSaveDto = OptionListSaveDto.builder()
			.questionId(question.getId())
			.options(optionSaveDtoList)
			.build();

		//when
		optionService.saveOptions(optionListSaveDto);
		List<Option> options = optionRepository.findOptionsByQuestion_Id(question.getId());

		//then
		assertThat(question.getOptions().size()).isEqualTo(optionsCnt);
		assertThat(options.size()).isEqualTo(optionsCnt);

		assertThat(options.get(0).getOptionContent()).isEqualTo("Test Content" + 0);
		assertThat(options.get(1).getOptionContent()).isEqualTo("Test Content" + 1);

	}

}