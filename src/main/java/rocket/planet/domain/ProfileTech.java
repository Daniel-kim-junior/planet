package rocket.planet.domain;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@IdClass(ProfileTechId.class)
@Builder
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class ProfileTech {
	@Id
	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "profile_uid", insertable = false, updatable = false, nullable = false, columnDefinition = "BINARY(16)")
	private Profile profile;

	@Id
	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "tech_uid", insertable = false, updatable = false, nullable = false, columnDefinition = "BINARY(16)")
	private Tech tech;
}
