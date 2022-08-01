package dnd.studyplanner.config;

import lombok.Builder;
import lombok.Data;

@Data
public class Message {

	private StatusCode status;
	private String message;
	private Object data;

	public Message() {
		this.status = StatusCode.BAD_REQUEST;
		this.data = null;
		this.message = null;
	}
}