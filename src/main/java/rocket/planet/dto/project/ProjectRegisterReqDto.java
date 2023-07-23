package rocket.planet.dto.project;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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

}
