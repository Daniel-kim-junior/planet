package rocket.planet.domain;

import static lombok.AccessLevel.*;
import static rocket.planet.dto.profile.ProfileDto.*;

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
import rocket.planet.dto.auth.AuthDto.BasicInputReqDto;

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
	private List<Org> org = new ArrayList<>();

	@OneToMany(mappedBy = "profile")
	private List<ProfileAuthority> authority = new ArrayList<>();

	@OneToMany(mappedBy = "profile")
	private List<UserProject> userProject = new ArrayList<>();

	@OneToMany(mappedBy = "profile")
	private List<PjtRecord> extPjtRecord = new ArrayList<>();

	@OneToMany(mappedBy = "profile")
	private List<Certification> certification = new ArrayList<>();

	@OneToMany(mappedBy = "profile")
	private List<ProfileTech> profileTech = new ArrayList<>();

	@OneToMany(mappedBy = "owner")
	private List<ProfileVisitor> profileOwner = new ArrayList<>();

	@OneToMany(mappedBy = "visitor")
	private List<ProfileVisitor> profileVisitor = new ArrayList<>();

	@Column(nullable = false)
	private LocalDate profileBirthDt;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Column
	private boolean profileDisplay;

	@Column(nullable = false)
	private String userName;

	@Column
	private int profileCareer;

	@Column(nullable = false, unique = true)
	private String userNickName;

	@Column
	private LocalDate profileStartDate;

	@Column
	private boolean profileAnnualStatus;

	@Column(nullable = false, unique = true)
	private String userId;

	@Column
	private boolean profileStatus;

	@Builder
	public Profile(LocalDate profileStartDate, LocalDate profileBirthDt, String userId, Role role,
		boolean profileDisplay,
		int profileCareer,
		boolean profileAnnualStatus, String userName, String userNickName, boolean profileStatus) {

		this.profileStartDate = profileStartDate;
		this.profileBirthDt = profileBirthDt;
		this.userName = userName;
		this.userId = userId;
		this.role = role;
		this.profileDisplay = profileDisplay;
		this.profileCareer = profileCareer;
		this.profileAnnualStatus = profileAnnualStatus;
		this.userNickName = userNickName;
		this.profileStatus = profileStatus;
	}

	public static Profile BasicInsertDtoToProfile(BasicInputReqDto dto, String id) {
		return builder()
			.profileBirthDt(dto.getUserBirth())
			.userId(id)
			.profileStartDate(dto.getCompanyJoinDate())
			.role(Role.CREW)
			.profileDisplay(dto.isProfileDisplay())
			.profileCareer(dto.getCareer())
			.profileAnnualStatus(false)
			.userName(dto.getUserName())
			.userNickName(idToUserNickName(id))
			.profileStatus(true)
			.build();
	}

	public static String idToUserNickName(String id) {
		return id.split("@")[0];
	}

	@Override
	public String toString() {
		return "Profile{" +
			"id=" + id +
			", 유저 닉네임" + userNickName +
			"유저 id=" + userId +
			"유저이름=" + userName +
			", 생년월일 =" + profileBirthDt +
			", 프로필 노출여부 =" + profileDisplay +
			", 경력 =" + profileCareer +
			", 휴가 여부 =" + profileAnnualStatus +
			'}';
	}

	public void updateProfile(ProfileUpDateResDto updateDto) {
		this.userName = updateDto.getUserName();
		this.profileStartDate = updateDto.getProfileStartDate();
		this.profileBirthDt = updateDto.getProfileBirthDt();
		this.profileCareer = updateDto.getProfileCareer();
	}

	public void updateDisplay(ProfileDisplayUpDateResDto displayDto) {
		this.profileDisplay = displayDto.isProfileDisplay();
	}

	public void updateAnnual(AnnualUpDateResDto annualDto) {
		this.profileAnnualStatus = annualDto.isProfileAnnualStatus();
	}

	public void updateRole(String role) {
		this.role = Role.valueOf(role);

	}

	public void updateRetiredProfile() {
		this.profileStatus = false;
		this.org = null;

	}

}
