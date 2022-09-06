package dnd.studyplanner.domain.goal.model;

import java.time.LocalDate;
import java.util.Comparator;

import static java.time.Period.between;

public class GoalEndDateComparator implements Comparator<Goal> {
    @Override
    public int compare(Goal o1, Goal o2) {
        LocalDate today = LocalDate.now();
        return String.valueOf(between(today, o1.getGoalEndDate()).getDays()).compareTo(String.valueOf(between(today, o2.getGoalEndDate())));
    }
}