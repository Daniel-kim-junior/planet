package rocket.planet.domain;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ProfileTech {

	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "profile_tech_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "profile_uid", updatable = false, nullable = false)
	private Profile profile;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "tech_uid", updatable = false, nullable = false)
	private Tech tech;

	@Builder
	public ProfileTech(Profile profile, Tech tech) {
		this.id = id;
		this.profile = profile;
		this.tech = tech;
	}
}
