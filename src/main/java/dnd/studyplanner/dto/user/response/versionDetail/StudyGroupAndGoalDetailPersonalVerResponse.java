package dnd.studyplanner.dto.user.response.versionDetail;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 개인 Ver 문제집 목록
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupAndGoalDetailPersonalVerResponse {

    private Long goalId;
    private boolean checkEditEnabled;   // 내가 출제한 문제집 수정 가능 여부 - 해당 문제집을 푼 사람이 아무도 없는 경우 수정 가능
    private String questionBookName;   // 해당 세부 목표의 문제집 리스트 ( 본인이 출제한 문제집 제외 ) INDEX -> 문제집 + INDEX
    private String questionBookCreateUserName;   // 각 문제집을 출제한 사람 이름
    private int questionNumPerQuestionBook;   // 문제집 내의 문제 수
    private int answerNumPerQuestionBook;   // 문제집 내의 사용자가 맞춘 정답 수
    private boolean checkCompleteToSolve;   // 각 문제집 마다 (현재 사용자 기준) 풀이 완료 여부 - 이때 풀이는, 단순히 해당 문제집을 풀었냐/안 풀었냐에 대한 응시 여부


    @Builder
    public StudyGroupAndGoalDetailPersonalVerResponse(Goal goal, QuestionBook questionBook, boolean checkEditEnabled,
                                        int questionNumPerQuestionBook, int answerNumPerQuestionBook, boolean checkCompleteToSolve) {
        this.goalId = goal.getId();
        this.questionBookName = questionBook.getQuestionBookName();
        this.questionBookCreateUserName = questionBook.getQuestionBookCreateUser().getUserNickName();
        this.checkEditEnabled = checkEditEnabled;   // userSolveQuestionBook.isSolved() >= 1 인 경우 false
        this.questionNumPerQuestionBook = questionNumPerQuestionBook;   // 세부 목표 ID 가 id 인 QuestionBook 의 개수
        this.answerNumPerQuestionBook = answerNumPerQuestionBook;
        this.checkCompleteToSolve = checkCompleteToSolve;

    }

}
