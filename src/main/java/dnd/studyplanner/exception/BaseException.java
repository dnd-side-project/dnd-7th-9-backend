package dnd.studyplanner.exception;

import dnd.studyplanner.dto.response.CustomResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseException extends Exception {
	private CustomResponseStatus status;
}
