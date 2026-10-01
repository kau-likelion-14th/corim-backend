package likelion14th.lte.statistic.entity;

import jakarta.persistence.*;
import likelion14th.lte.todo.entity.WeekEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stat_week")
public class StatWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WeekEnum week;

    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statistic_id")
    private Statistic statistic;

    protected StatWeek(WeekEnum week, Statistic statistic) {
        this.week = week;
        this.count = 0;
        this.statistic = statistic;
    }

    public void addCount() {
        this.count++;
    }
}