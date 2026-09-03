package likelion14th.lte.statistic.service;

import jakarta.persistence.EntityManager;
import likelion14th.lte.global.api.ErrorCode;
import likelion14th.lte.global.exception.GeneralException;
import likelion14th.lte.statistic.dto.response.StatisticResponse;
import likelion14th.lte.statistic.entity.StatWeek;
import likelion14th.lte.statistic.entity.Statistic;
import likelion14th.lte.todo.repository.TodoDateRepository;
import likelion14th.lte.user.entity.User;
import likelion14th.lte.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final UserRepository userRepository;
    private final TodoDateRepository todoDateRepository;
    private final EntityManager entityManager;

    /*@@@@@@@@@@@ 과제 1 @@@@@@@@@@@*/
    @Transactional(readOnly = true)
    public StatisticResponse getStatistic(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        Statistic statistic = user.getStatistic();
        return StatisticResponse.from(statistic);
    }

    /*@@@@@@@@@@@ 과제 3 @@@@@@@@@@@*/

    @Transactional
    public void updateAllStatistics() {
        int page = 0;
        int size = 500;
        Page<User> userPage;

        do {

            userPage = userRepository.findAll(PageRequest.of(page, size));

            for (User user : userPage.getContent()) {
                updateStatistic(user);
            }

            entityManager.flush();
            entityManager.clear();
            page++;

        } while (userPage.hasNext());
    }

    /*@@@@@@@@@@@ 과제 2 @@@@@@@@@@@*/
    private void updateStatistic(User user) {
        Statistic statistic = user.getStatistic();


        if (statistic == null) {
            return;
        }


        LocalDate yesterday = LocalDate.now().minusDays(1);
        Long userId = user.getId();


        boolean hasSuccess = todoDateRepository.existsByTodo_User_IdAndDateAndCompleted(userId, yesterday, true);
        boolean hasFail = todoDateRepository.existsByTodo_User_IdAndDateAndCompleted(userId, yesterday, false);
        statistic.updateStreak(hasSuccess, hasFail);

        if (hasSuccess && !hasFail) {
            for (StatWeek statWeek : statistic.getStatWeeks()) {
                if (statWeek.getWeek().toDayOfWeek() == yesterday.getDayOfWeek()) {
                    statWeek.addCount();
                    break;
                }
            }
        }

        LocalDate thirtyDaysAgo = yesterday.minusDays(30);
        long successCount = todoDateRepository.countByTodo_User_IdAndDateBetweenAndCompleted(userId, thirtyDaysAgo, yesterday, true);
        long failCount = todoDateRepository.countByTodo_User_IdAndDateBetweenAndCompleted(userId, thirtyDaysAgo, yesterday, false);
        long totalCount = successCount + failCount;

        statistic.updateMonthPercent(totalCount, successCount);
    }
}