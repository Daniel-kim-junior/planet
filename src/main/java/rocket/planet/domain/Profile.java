package rocket.planet.domain;

import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
public class Profile extends BaseTime {
	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "profile_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@OneToMany(mappedBy = "profile")
	private List<PjtRecord> extPjtRecord;

	@OneToMany(mappedBy = "profile")
	private List<Certification> certification;

	@OneToMany(mappedBy = "profile")
	private List<ProfileTech> profileTech;

	@Column(nullable = false)
	private LocalDate profileBirthDt;

	@Column
	private boolean profileDisplay;

	@Column
	private int profileCareer;

	@Column
	private boolean profileAnnualStatus;

	@Override
	public String toString() {
		return "Profile{" +
			"id=" + id +
			", 생년월일 =" + profileBirthDt +
			", 프로필 노출여부 =" + profileDisplay +
			", 경력 =" + profileCareer +
			", 휴가 여부 =" + profileAnnualStatus +
			'}';
	}
}
