package rocket.planet.dto.project;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectRegisterReqDto {
	private String userNickName;
	private String projectName;
	private String projectDesc;
	private String projectTech;
	private LocalDate projectStartDt;
	private LocalDate projectEndDt;
	private String projectLeader;
	private List<String> projectMember;
}
