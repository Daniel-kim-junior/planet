package rocket.planet.dto.project;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectRegisterReqDto {
	private String userNickName;
	private String projectName;
	private String projectDesc;
	private String projectTech;
	private String deptName;
	private String teamName;
	private LocalDate projectStartDt;
	private LocalDate projectEndDt;
	private String projectLeader;
	private List<String> projectMember;
	private String authType;

	@Builder
	public ProjectRegisterReqDto(String userNickName, String projectName, String projectDesc, String projectTech,
		String deptName, String teamName, LocalDate projectStartDt, LocalDate projectEndDt, String projectLeader,
		List<String> projectMember, String authType) {
		this.userNickName = userNickName;
		this.projectName = projectName;
		this.projectDesc = projectDesc;
		this.projectTech = projectTech;
		this.deptName = deptName;
		this.teamName = teamName;
		this.projectStartDt = projectStartDt;
		this.projectEndDt = projectEndDt;
		this.projectLeader = projectLeader;
		this.projectMember = projectMember;
		this.authType = authType;
	}
}
