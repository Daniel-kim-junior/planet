package rocket.planet.dto.Search;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
