package rocket.planet.domain;

import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Profile extends BaseTime {
	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "profile_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@OneToMany(mappedBy = "profile")
	private List<Authority> authority = new ArrayList<>();

	@OneToMany(mappedBy = "profile")
	private List<UserProject> userProject = new ArrayList<>();

	@OneToMany(mappedBy = "profile")
	private List<PjtRecord> extPjtRecord = new ArrayList<>();

	@OneToMany(mappedBy = "profile")
	private List<Certification> certification = new ArrayList<>();

	@OneToMany(mappedBy = "profile")
	private List<ProfileTech> profileTech = new ArrayList<>();

	@Column(nullable = false)
	private LocalDate profileBirthDt;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	@Column
	private boolean profileDisplay;

	@Column
	private int profileCareer;

	@Column
	private boolean profileAnnualStatus;

	@Builder
	public Profile(LocalDate profileBirthDt, Role role, boolean profileDisplay, int profileCareer,
		boolean profileAnnualStatus) {
		this.profileBirthDt = profileBirthDt;
		this.role = role;
		this.profileDisplay = profileDisplay;
		this.profileCareer = profileCareer;
		this.profileAnnualStatus = profileAnnualStatus;
	}

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
