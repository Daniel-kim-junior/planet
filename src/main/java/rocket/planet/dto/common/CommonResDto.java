package rocket.planet.dto.common;

import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class CommonResDto {
    private String message;
}
