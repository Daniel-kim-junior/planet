package rocket.planet.service.project;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import rocket.planet.domain.ProjectStatus;
import rocket.planet.repository.jpa.ProjectRepository;

@Service
@RequiredArgsConstructor
public class SchedulerService {
	private final ProjectRepository projectRepository;

	@Scheduled(cron = "0 0 0 * * ?")
	@Transactional
	public void updateProjectStatus() {
		projectRepository.findByProjectStatusIs(ProjectStatus.WAITING)
			.stream()
			.filter(project -> project.getProjectStartDt().isEqual(LocalDate.now()))
			.forEach(project -> project.toUpdateProjectStatusDto(project));
	}
}
