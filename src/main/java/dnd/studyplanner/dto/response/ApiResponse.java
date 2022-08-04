package dnd.studyplanner.dto.response;

import static dnd.studyplanner.dto.response.ApiResponseStatus.*;

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
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {

	@JsonProperty("isSuccess")
	private final Boolean isSuccess;

	private final int code;
	private final String message;
	private final HttpStatus httpStatus;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T result;

	public ApiResponse(T result) {
		this.isSuccess = SUCCESS.isSuccess();
		this.message = SUCCESS.getMessage();
		this.code = SUCCESS.getCode();
		this.result = result;
		this.httpStatus = HttpStatus.OK;
	}

	public ApiResponse(ApiResponseStatus status) {
		this.isSuccess = status.isSuccess();
		this.message = status.getMessage();
		this.code = status.getCode();
		this.httpStatus = status.getHttpStatus();
	}

	public ResponseEntity<ApiResponse> toResponseEntity() {
		return new ResponseEntity<>(this, this.httpStatus);
	}

	public ResponseEntity<ApiResponse> toResponseEntity(HttpHeaders httpHeaders) {
		return new ResponseEntity<>(this, httpHeaders, this.httpStatus);
	}
}
