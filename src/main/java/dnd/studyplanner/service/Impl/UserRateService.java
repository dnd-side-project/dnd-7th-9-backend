package dnd.studyplanner.service.Impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.questionbook.model.QuestionBook;
import dnd.studyplanner.domain.user.model.User;
import dnd.studyplanner.domain.user.model.UserGoalRate;
import dnd.studyplanner.jwt.JwtService;
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

	@Override
	public UserGoalRate updateAfterQuestionBook(String accessToken, Long questionBookId) {
		Long userId = jwtService.getUserId(accessToken);
		Optional<User> user = userRepository.findById(userId);
		Optional<QuestionBook> questionBook = questionBookRepository.findById(questionBookId);
		Goal goal = questionBook.get().getQuestionBookGoal();

		UserGoalRate userGoalRate = userGoalRateRepository.findByUser_IdAndGoal_Id(userId, goal.getId()).orElse(
			UserGoalRate.builder()
				.user(user.get())
				.goal(goal)
				.build());
		userGoalRate.updateQuestionBookSolve(goal.getRatePerQuestionBook());
	}

	@Override
	public UserGoalRate getUserGoalRateByQuestionBookId(String accessToken, Long questionBookId) {
		Long userId = jwtService.getUserId(accessToken);

		Optional<User> user = userRepository.findById(userId);
		Optional<QuestionBook> questionBook = questionBookRepository.findById(questionBookId);
		Goal goal = questionBook.get().getQuestionBookGoal();

		return userGoalRateRepository.findByUser_IdAndGoal_Id(userId, goal.getId()).orElse(
			UserGoalRate.builder()
				.user(user.get())
				.goal(goal)
				.build()
		);
	}
}
