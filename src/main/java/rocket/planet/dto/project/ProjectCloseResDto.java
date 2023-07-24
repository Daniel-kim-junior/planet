package rocket.planet.dto.project;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectCloseResDto {
	private String projectName;
	private String userNickName;
	private String projectLeader;
	private LocalDate projectStartDt;
	private LocalDate projectEndDt;

}
