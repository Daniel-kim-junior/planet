package rocket.planet.service.project;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.domain.Project;
import rocket.planet.domain.ProjectStatus;
import rocket.planet.dto.project.ProjectRegisterResDto;
import rocket.planet.repository.jpa.ProjectRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulerService {
	private final ProjectRepository projectRepository;

	@Scheduled(cron = "0 0 0 * * ?")
	public void updateProjectStatus(){
		projectRepository.findByProjectStatusIs(ProjectStatus.WAITING)
			.stream()
			.filter(project -> project.getProjectStartDt().isEqual(LocalDate.now()))
			.forEach(project -> project.toUpdateProjectStatusDto(project));
	}
}