package dnd.studyplanner.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnd.studyplanner.dto.option.request.OptionListSaveDto;
import dnd.studyplanner.exception.BaseException;
import dnd.studyplanner.service.Impl.OptionService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/option")
@RestController
public class OptionController {

	private final OptionService optionService;

	@PostMapping("/list")
	public void addOptionAsList(OptionListSaveDto saveDto) throws BaseException {
		optionService.saveOptions(saveDto);
	}
}
