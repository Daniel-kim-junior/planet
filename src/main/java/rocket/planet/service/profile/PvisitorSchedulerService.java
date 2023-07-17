package rocket.planet.service.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocket.planet.domain.ProfileVisitor;
import rocket.planet.repository.jpa.PvisitorRepository;

import java.time.LocalDate;
import java.util.List;



@Service
@RequiredArgsConstructor

public class PvisitorSchedulerService {
    private final PvisitorRepository pvisitorRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteExpiredProfileVisitors() {
        LocalDate fifteenDaysAgo = LocalDate.now().minusDays(15);
        List<ProfileVisitor> expiredVisitors = pvisitorRepository.findByVisitTimeBefore(fifteenDaysAgo);
        pvisitorRepository.deleteInBatch(expiredVisitors);
    }

}
