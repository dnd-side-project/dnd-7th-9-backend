package dnd.studyplanner.dto.option.request;

import java.util.ArrayList;
import java.util.List;

import dnd.studyplanner.domain.option.model.Option;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionListSaveDto {
	private Long questionId;
	private List<OptionSaveDto> options = new ArrayList<>();

	@Builder
	public OptionListSaveDto(Long questionId, List<OptionSaveDto> options) {
		this.questionId = questionId;
		this.options = options;
	}
}
