package rocket.planet.domain;

import static lombok.AccessLevel.*;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
public class Authority extends BaseTime {
	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "auth_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AuthType authType;

	@Column(columnDefinition = "BINARY(16)")
	private UUID authTargetId;

	@Column(nullable = false)
	@Email
	private String authorizerId;

	@Override
	public String toString() {
		return "권한{" +
			"권한 uuid=" + id +
			", 권한 테이블 타입=" + authType +
			", 부여 권한 uuid='" + authTargetId + '\'' +
			", 권한 부여자 id(email)='" + authorizerId + '\'' +
			'}';
	}
}
