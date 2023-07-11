package rocket.planet.dto.org;

import lombok.Builder;
import lombok.Getter;
import rocket.planet.domain.OrgType;

import java.util.UUID;

@Getter
@Builder
public class DeptReqDto {
    private UUID id;
    private String deptName;
    private OrgType deptType;

    private CompanyReqDto company;

    public String getDeptName() {
        return deptName;
    }
}
