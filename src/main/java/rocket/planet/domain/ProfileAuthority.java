package rocket.planet.domain;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

import java.util.UUID;

import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ProfileAuthority {
	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "profile_auth_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "profile_uid", nullable = false)
	private Profile profile;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "auth_uid", nullable = false)
	private Authority authority;

	@Builder
	public ProfileAuthority(Profile profile, Authority authority) {
		this.profile = profile;
		this.authority = authority;
	}

	@Override
	public String toString() {
		return "ProfileAuthority{" +
				"id=" + id +
				", 프로필 UUID=" + profile +
				", 권한 UUID=" + authority +
				'}';
	}
}
