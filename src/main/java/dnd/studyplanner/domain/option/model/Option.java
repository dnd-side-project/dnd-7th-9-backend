package dnd.studyplanner.domain.option.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import dnd.studyplanner.domain.base.BaseEntity;
import dnd.studyplanner.domain.question.model.Question;
import dnd.studyplanner.dto.option.response.OptionResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "options")
public class Option extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "option_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id")
	private Question question;

	private String optionContent;
	private boolean optionImageEnable;
	private String optionImageUrl;

	@Builder
	public Option(Question question, String optionContent, boolean optionImageEnable, String optionImageUrl) {
		this.question = question;
		this.optionContent = optionContent;
		this.optionImageEnable = optionImageEnable;
		this.optionImageUrl = optionImageUrl;

		question.getOptions().add(this);
	}

	public OptionResponseDto toResponseDto() {
		return OptionResponseDto.builder()
			.option(this)
			.build();
	}

}
