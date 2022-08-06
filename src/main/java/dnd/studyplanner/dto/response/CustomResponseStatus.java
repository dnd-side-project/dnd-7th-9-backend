package dnd.studyplanner.dto.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum CustomResponseStatus {

	SUCCESS(true, 1000, "요청에 성공하였습니다", HttpStatus.OK),
	UPLOAD_IMAGE_SUCCESS(true, 1001, "이미지 업로드 성공", HttpStatus.OK),
	DELETE_IMAGE_SUCCESS(true, 1001, "이미지 삭제 성공", HttpStatus.OK),
	SAVE_QUESTION_BOOK_SUCCESS(true, 1100, "문제집 저장 성공", HttpStatus.OK),
	SAVE_USER_SUCCESS(true, 1200, "사용자 정보 저장 성공", HttpStatus.OK),
	SAVE_GROUP_SUCCESS(true, 1300, "그룹 정보 저장 성공", HttpStatus.OK),
	SAVE_GOAL_SUCCESS(true, 1400, "목표 저장 성공", HttpStatus.OK),

	//4000번 오류 응답코드
	REQUEST_DATA_NULL(false, 4000, "필수 항목이 입력되지 않았습니다", HttpStatus.BAD_REQUEST),
	TOKEN_INVALID(false, 4001, "유효하지 않은 토큰입니다", HttpStatus.CONFLICT),
	TOKEN_EXPIRED(false, 4002, "만료된 토큰입니다", HttpStatus.UNAUTHORIZED);


	private final boolean isSuccess;
	private final int responseCode;
	private final String message;

	private final HttpStatus httpStatus;

	CustomResponseStatus(boolean isSuccess, int responseCode, String message, HttpStatus httpStatus) {
		this.isSuccess = isSuccess;
		this.responseCode = responseCode;
		this.message = message;
		this.httpStatus = httpStatus;
	}
}
