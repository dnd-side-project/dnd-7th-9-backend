package dnd.studyplanner.dto.option.response;

import dnd.studyplanner.domain.option.model.Option;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionSolvedDetailResponseDto {
	private Long optionId;
	private String optionContent;
	private String optionImageUrl;
	private Boolean isChecked;
	private Boolean isAnswerOption;

	@Builder
	public OptionSolvedDetailResponseDto(Option option, Boolean isChecked) {
		this.optionId = option.getId();
		this.optionContent = option.getOptionContent();
		this.optionImageUrl = option.getOptionImageUrl();
		this.isAnswerOption = option.isAnswer();
		this.isChecked = isChecked;
	}
}
