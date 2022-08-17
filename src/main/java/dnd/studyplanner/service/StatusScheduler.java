package dnd.studyplanner.service;

import static dnd.studyplanner.domain.studygroup.model.StudyGroupStatus.*;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import dnd.studyplanner.domain.goal.model.Goal;
import dnd.studyplanner.domain.goal.model.GoalStatus;
import dnd.studyplanner.domain.studygroup.model.StudyGroup;
import dnd.studyplanner.repository.GoalRepository;
import dnd.studyplanner.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatusScheduler {

	private final StudyGroupRepository studyGroupRepository;
	private final GoalRepository goalRepository;

	@Scheduled(cron = "0 0 12 * * *")
	@Transactional
	public void updateStudyGroupStatus() {
		LocalDate today = LocalDate.now();

		List<StudyGroup> studyGroupList = studyGroupRepository.findAll();
		studyGroupList.forEach(e -> {
			log.debug("[배치 전 그룹 상태] : {}", e.getGroupStatus());
			LocalDate groupStartDate = e.getGroupStartDate();
			LocalDate groupEndDate = e.getGroupEndDate();
			int compareStatus = groupStartDate.compareTo(today);

			if (groupEndDate.isBefore(today)) {
				e.updateStatus(COMPLETE);
			} else if (compareStatus > 0) {
				e.updateStatus(READY);
			} else if (compareStatus <= 0) {
				e.updateStatus(ACTIVE);
			}
			log.debug("[배치 후 그룹 상태] : {}", e.getGroupStatus());
		});

		studyGroupRepository.saveAll(studyGroupList);
	}

	@Scheduled(cron = "0 0 12 * * *")
	@Transactional
	public void updateGoalStatus() {
		LocalDate today = LocalDate.now();

		List<Goal> goalList = goalRepository.findAll();
		goalList.forEach(e -> {
			log.debug("[배치 전 목표 상태] : {}", e.getGoalStatus());
			LocalDate goalStartDate = e.getGoalStartDate();
			LocalDate goalEndDate = e.getGoalEndDate();
			int compareStatus = goalStartDate.compareTo(today);

			if (goalEndDate.isBefore(today)) {
				e.updateStatus(GoalStatus.COMPLETE);
			} else if (compareStatus > 0) {
				e.updateStatus(GoalStatus.READY);
			} else if (compareStatus <= 0) {
				e.updateStatus(GoalStatus.ACTIVE);
			}
			log.debug("[배치 후 목표 상태] : {}", e.getGoalStatus());
		});

		goalRepository.saveAll(goalList);
	}
}
