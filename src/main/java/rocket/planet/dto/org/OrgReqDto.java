package rocket.planet.dto.org;

import lombok.Builder;
import lombok.Getter;
import rocket.planet.dto.profile.ProfileDto;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class OrgReqDto {
    private UUID id;
    private LocalDate belongStartDate;
    private LocalDate belongEndDate;
    private String belongInviter;
    private boolean belongStatus;
    private String deptName;
    private String teamName;

    private ProfileDto profile;
    private CompanyReqDto company;
    private DeptReqDto department;
    private TeamReqDto team;


}
