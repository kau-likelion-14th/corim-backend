package likelion14th.lte.statistic.entity;

import jakarta.persistence.*;
import likelion14th.lte.todo.entity.WeekEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "statistic")
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statistic_id")
    private Long id;

    private int streak;

    @Column(name = "month_percent")
    private int monthPercent;

    @OneToMany(mappedBy = "statistic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StatWeek> statWeeks = new ArrayList<>();

    public static Statistic create() {
        Statistic statistic = new Statistic();
        statistic.streak = 0;
        statistic.monthPercent = 0;
        statistic.initializeWeeks();
        return statistic;
    }

    private void initializeWeeks() {
        for (WeekEnum week : WeekEnum.values()) {
            this.statWeeks.add(new StatWeek(week, this));
        }
    }

    /*@@@@@@@@@@@ 과제 1 @@@@@@@@@@@*/
    public WeekEnum getMostTodoWeek() {
        return statWeeks.stream()
                .max(Comparator.comparingInt(StatWeek::getCount))
                .map(StatWeek::getWeek)
                .orElse(WeekEnum.MON);
    }

    /*@@@@@@@@@@@ 과제 2 @@@@@@@@@@@*/
    public void updateStreak(boolean hasSuccess, boolean hasFail) {
        if (hasSuccess && !hasFail) {
            this.streak += 1;
        } else {
            this.streak = 0;
        }
    }

    /*@@@@@@@@@@@ 과제 2 @@@@@@@@@@@*/
    public void updateMonthPercent(long totalCount, long successCount) {
        if (totalCount == 0) {
            this.monthPercent = 0;
        } else {
            this.monthPercent = (int) ((successCount * 100) / totalCount);
        }
    }
}