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
	GET_GROUP_SUCCESS(true, 1301, "사용자 가입 그룹 조회 성공", HttpStatus.OK),
	GET_MY_GROUP_SUCCESS(true, 1302, "프로필 스터디 그룹 조회 성공", HttpStatus.OK),
	GET_GROUP_DETAIL_SUCCESS(true, 1303, "스터디 그룹 상세 정보 조회 성공", HttpStatus.OK),
	INVITE_USER_SUCCESS(true, 1304, "사용자 그룹 초대 성공", HttpStatus.OK),
	SAVE_GOAL_SUCCESS(true, 1400, "목표 저장 성공", HttpStatus.OK),
	FINISH_STUDY_GROUP_SUCCESS(true, 1500, "스터디 그룹이 완료 처리 되었습니다", HttpStatus.OK),

	//4000번 오류 응답코드
	REQUEST_DATA_NULL(false, 4000, "필수 항목이 입력되지 않았습니다", HttpStatus.BAD_REQUEST),
	TOKEN_INVALID(false, 4001, "유효하지 않은 토큰입니다", HttpStatus.CONFLICT),
	TOKEN_EXPIRED(false, 4002, "만료된 토큰입니다", HttpStatus.UNAUTHORIZED),
	NOT_VALID_USER(false, 4003, "유효하지 않는 이메일 형식입니다", HttpStatus.BAD_REQUEST),
	NOT_EXIST_USER(false, 4004, "존재하지 않는 사용자입니다", HttpStatus.NOT_FOUND),
	NOT_VALID_STATUS(false, 4005, "옳바르지 않는 스터디 그룹 상태 입니다", HttpStatus.BAD_REQUEST),
	TOKEN_NULL(false, 4006, "토큰이 존재하지 않습니다", HttpStatus.UNAUTHORIZED),
	USER_NOT_IN_GROUP(false, 4007, "사용자가 가입한 그룹이 아닙니다", HttpStatus.BAD_REQUEST),
	USER_ALREADY_IN_GROUP(false, 4008, "이미 가입한 그룹입니다", HttpStatus.ALREADY_REPORTED),
	START_AFTER_END(false, 4009, "시작 날짜는 종료 날짜보다 이전일 수 없습니다", HttpStatus.BAD_REQUEST),
	GOAL_EXCEED_GROUP(false, 4010, "기간별 목표는 그룹의 활동일을 벗어날 수 없습니다", HttpStatus.BAD_REQUEST),
	NOT_EXIST_DATA(false, 4100, "존재하지 않는 데이터입니다.", HttpStatus.BAD_REQUEST),
	UNAUTHORIZED_REQUEST(false, 4101, "권한이 없는 요청입니다.", HttpStatus.UNAUTHORIZED),

	ALREADY_SOLVED_QUESTION_BOOK(false, 4200, "이미 풀이 완료한 문제집 입니다", HttpStatus.BAD_REQUEST),
	UNAUTHORIZED_QUESTION_BOOK(false, 4201, "풀이 권한이 없는 문제집 입니다", HttpStatus.BAD_REQUEST);

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
