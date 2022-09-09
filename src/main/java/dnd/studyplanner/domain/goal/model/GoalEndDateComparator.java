package dnd.studyplanner.domain.goal.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

public class GoalEndDateComparator implements Comparator<Goal> {

    @Override
    public int compare(Goal o1, Goal o2) {
        LocalDate today = LocalDate.now();

        return String.valueOf(today.until(o1.getGoalEndDate(), ChronoUnit.DAYS))
                .compareTo(String.valueOf(today.until(o2.getGoalEndDate(), ChronoUnit.DAYS)));
    }
}