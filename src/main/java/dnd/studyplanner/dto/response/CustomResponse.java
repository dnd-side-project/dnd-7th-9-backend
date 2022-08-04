package dnd.studyplanner.dto.response;

import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "responseCode", "message", "result"})
public class CustomResponse<T> {

	@JsonProperty("isSuccess")
	private final Boolean isSuccess;

	private final int responseCode;
	private final String message;
	private final HttpStatus httpStatus;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T result;

	public CustomResponse(T result) { //Response Body 포함
		this.isSuccess = SUCCESS.isSuccess();
		this.message = SUCCESS.getMessage();
		this.responseCode = SUCCESS.getResponseCode();
		this.result = result;
		this.httpStatus = HttpStatus.OK;
	}

	public CustomResponse(T result, CustomResponseStatus status) { //원하는 성공 메세지 추가하여 응답
		this.isSuccess = status.isSuccess();
		this.message = status.getMessage();
		this.responseCode = status.getResponseCode();
		this.httpStatus = status.getHttpStatus();
		this.result = result;
	}

	public CustomResponse(CustomResponseStatus status) {
		this.isSuccess = status.isSuccess();
		this.message = status.getMessage();
		this.responseCode = status.getResponseCode();
		this.httpStatus = status.getHttpStatus();
	}

	public ResponseEntity<CustomResponse> toResponseEntity() {
		return new ResponseEntity<>(this, this.httpStatus);
	}

	public ResponseEntity<CustomResponse> toResponseEntity(HttpHeaders httpHeaders) {
		return new ResponseEntity<>(this, httpHeaders, this.httpStatus);
	}
}
