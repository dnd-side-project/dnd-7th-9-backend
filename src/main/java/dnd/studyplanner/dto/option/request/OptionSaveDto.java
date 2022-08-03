package dnd.studyplanner.dto.option.request;

import dnd.studyplanner.domain.option.model.Option;
import dnd.studyplanner.domain.question.model.Question;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionSaveDto {
	private String optionContent;
	private boolean optionImageEnable;
	private String optionImageUrl;

	@Builder
	public OptionSaveDto(String optionContent, boolean optionImageEnable, String optionImageUrl) {
		this.optionContent = optionContent;
		this.optionImageEnable = optionImageEnable;
		this.optionImageUrl = optionImageUrl;
	}

	public Option toEntity(Question question) {
		return Option.builder()
			.question(question)
			.optionContent(this.optionContent)
			.optionImageEnable(this.optionImageEnable)
			.optionImageUrl(this.optionImageUrl)
			.build();
	}
}
