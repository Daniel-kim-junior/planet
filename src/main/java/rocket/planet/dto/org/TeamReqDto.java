package rocket.planet.dto.org;

import lombok.Builder;
import lombok.Getter;
import rocket.planet.domain.OrgType;

import java.util.UUID;

@Getter
@Builder
public class TeamReqDto {
    private UUID id;
    private String teamName;
    private String teamDesc;
    private OrgType teamType;

    private DeptReqDto department;
    private TeamReqDto team;
}
