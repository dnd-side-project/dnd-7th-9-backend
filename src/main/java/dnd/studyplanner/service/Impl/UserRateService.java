package dnd.studyplanner.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.domain.user.model.UserGoalRate;
import dnd.studyplanner.jwt.JwtService;
import dnd.studyplanner.repository.GoalRepository;
import dnd.studyplanner.repository.QuestionBookRepository;
import dnd.studyplanner.repository.UserGoalRateRepository;
import dnd.studyplanner.repository.UserRepository;
import dnd.studyplanner.service.IUserRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserRateService implements IUserRateService {

	private final JwtService jwtService;

	private final UserGoalRateRepository userGoalRateRepository;
	private final QuestionBookRepository questionBookRepository;
	private final UserRepository userRepository;
	private final GoalRepository goalRepository;

	@Override
	public UserGoalRate updateAfterQuestionBook(UserGoalRate userGoalRate) {
		int minSolveQuestionBook = userGoalRate.getGoal().getMinSolveQuestionBook();

		int ratePerQuestionBook = (50 / minSolveQuestionBook);
		userGoalRate.updateQuestionBookSolve(ratePerQuestionBook);
		userGoalRateRepository.save(userGoalRate);
		return userGoalRate;
	}

	@Override
	public UserGoalRate getUserGoalRateByQuestionBookId(String accessToken, Long questionBookId) {
		Optional<QuestionBook> questionBook = questionBookRepository.findById(questionBookId);
		Goal goal = questionBook.get().getQuestionBookGoal();

		return getUserGoalRate(accessToken, goal);
	}

	@Override
	public UserGoalRate updatePostQuestionBook(String accessToken, Long goalId) {
		Goal goal = goalRepository.findById(goalId).get();
		UserGoalRate userGoalRate = getUserGoalRate(accessToken, goal);
		userGoalRate.updatePostQuestionBook();

		return userGoalRate;
	}

	@Override
	public int getUserStudyGroupRate(String accessToken, Long studyGroupId) {
		Long userId = jwtService.getUserId(accessToken);
		double userGoalRateOfStudyGroup = userGoalRateRepository.findAll().stream()
			.filter(o -> o.getUser().getId().equals(userId))
			.filter(o -> o.getGoal().getStudyGroup().getId().equals(studyGroupId))
			.mapToDouble(UserGoalRate::getAchieveRate)
			.average()
			.orElse(0D);
		return (int) userGoalRateOfStudyGroup;
	}

	private UserGoalRate getUserGoalRate(String accessToken, Goal goal) {
		Long userId = jwtService.getUserId(accessToken);
		Optional<User> user = userRepository.findById(userId);

		return userGoalRateRepository.findByUser_IdAndGoal_Id(userId, goal.getId()).orElse(
			UserGoalRate.builder()
				.user(user.get())
				.goal(goal)
				.build());
	}
}
