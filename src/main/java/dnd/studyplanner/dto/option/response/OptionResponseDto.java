package dnd.studyplanner.dto.option.response;

import dnd.studyplanner.domain.option.model.Option;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionResponseDto {
	private Long optionId;
	private String optionContent;
	private boolean optionImageEnable;
	private String optionImageUrl;

	@Builder
	public OptionResponseDto(Option option) {
		this.optionId = option.getId();
		this.optionContent = option.getOptionContent();
		this.optionImageEnable = option.isOptionImageEnable();
		this.optionImageUrl = option.getOptionImageUrl();
	}
}
