package rocket.planet.dto.Search;

import lombok.*;

import java.util.Date;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@ToString
public class SearchUserReqDto {
    private String userId;
    private String teamName;
    private String techName;
    private String teamType;
    private String projectStatus;

}
