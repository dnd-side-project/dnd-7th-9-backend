package dnd.studyplanner.domain.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import dnd.studyplanner.domain.goal.model.Goal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGoalRate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_goal_rate_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "goal_id")
	private Goal goal;

	private int achieveRate;
	private int postRate;
	private int solveRate;

	private boolean isFinishGoal;

	@Builder
	public UserGoalRate(User user, Goal goal) {
		this.user = user;
		this.goal = goal;
		this.achieveRate = 0;
		this.postRate = 0;
		this.solveRate = 0;
		this.isFinishGoal = false;
	}

	public void updateQuestionBookSolve(int addRate) {
		if (this.isFinishGoal) {
			return;
		}

		achieveRate += addRate;
		solveRate += addRate;
		checkFinishGoal();
	}

	private void checkFinishGoal() {
		if (this.achieveRate >= 100) {
			isFinishGoal = true;
			achieveRate = 100;
		}
	}
}
