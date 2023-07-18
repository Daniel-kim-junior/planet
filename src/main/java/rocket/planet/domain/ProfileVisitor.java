package rocket.planet.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import rocket.planet.dto.profile.ProfileDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "profile_visitor")
@NoArgsConstructor(access = PROTECTED)
public class ProfileVisitor {
	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "visitor_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(optional = false, fetch = LAZY)
	@JoinColumn(columnDefinition = "BINARY(16)", name = "profile_owner_uid", nullable = false)
	private Profile owner;

	@ManyToOne(optional = false, fetch = LAZY)
	@JoinColumn(columnDefinition = "BINARY(16)", name = "profile_visitor_uid", nullable = false)
	private Profile visitor;

	@CreationTimestamp
	@Column(nullable = false)
	private LocalDate visitTime;

	@Builder
	public ProfileVisitor(Profile owner, Profile visitor, LocalDate visitTime) {
		this.owner = owner;
		this.visitor = visitor;
		this.visitTime = visitTime;
	}

	@Override
	public String toString() {
		return "ProfileVisitor{" +
				"프로필 주인장 =" + owner +
				", 프로필 방문객 =" + visitor +
				", 방문시간 =" + visitTime +
				'}';
	}

	public void updateVisitTime() {
		this.visitTime = LocalDate.now();
	}

}

