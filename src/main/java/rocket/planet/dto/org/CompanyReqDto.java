package rocket.planet.dto.org;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CompanyReqDto {
    private UUID id;
    private String companyName;
}
