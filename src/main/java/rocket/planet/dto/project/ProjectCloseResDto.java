package rocket.planet.dto.project;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ProjectCloseResDto {
	private String projectName;
	private String userName;
	private String projectLeader;
	private LocalDate projectStartDt;
	private LocalDate projectEndDt;

	@Builder
	public ProjectCloseResDto(String projectName, String userName, String projectLeader, LocalDate projectStartDt,
		LocalDate projectEndDt) {
		this.projectName = projectName;
		this.userName = userName;
		this.projectLeader = projectLeader;
		this.projectStartDt = projectStartDt;
		this.projectEndDt = projectEndDt;
	}
}
