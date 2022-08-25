package dnd.studyplanner.dto.user.response.versionDetail;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.domain.user.model.UserSolveQuestionBook;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 팀 Ver 문제집 목록
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupAndGoalDetailTeamVerResponse {

    private Long goalId;
    private String questionBookName;   // 해당 세부 목표의 문제집 리스트 ( 본인이 출제한 문제집 제외 ) INDEX -> 문제집 + INDEX
    private String questionBookCreateUserName;   // 각 문제집을 출제한 사람 이름
    private int personNumOfCompleteToSolvePerQuestionBook;   // 각 문제집당 푼(응시를 한) 인원
    private List<String> personListOfCompleteToSolvePerQuestionBook;   // 각 문제집당 푼(응시를 한) 사람의 이름 목록


    @Builder
    public StudyGroupAndGoalDetailTeamVerResponse(Goal goal, QuestionBook questionBook,
        int personNumOfCompleteToSolvePerQuestionBook,
        List<String> personListOfCompleteToSolvePerQuestionBook) {

        this.goalId = goal.getId();
        this.questionBookName = questionBook.getQuestionBookName();
        this.questionBookCreateUserName = questionBook.getQuestionBookCreateUser().getUserNickName();
        this.personNumOfCompleteToSolvePerQuestionBook = personNumOfCompleteToSolvePerQuestionBook;   // userSolveQuestionBook.isSolved() 개수
        this.personListOfCompleteToSolvePerQuestionBook = personListOfCompleteToSolvePerQuestionBook;   // userSolveQuestionBook.isSolved() 인 사람의 이름 목록
    }
}
