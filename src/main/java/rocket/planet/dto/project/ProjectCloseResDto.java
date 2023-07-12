package rocket.planet.dto.project;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ProjectCloseResDto {
	private String projectName;
	private String userName;
	private String projectLeader;
	private LocalDate projectStartDt;
	private LocalDate projectEndDt;
}
