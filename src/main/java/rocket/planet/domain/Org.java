package rocket.planet.domain;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;
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
public class Org extends BaseTime {
	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "org_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "profile_uid", nullable = false)
	private Profile profile;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "company_uid")
	private Company company;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "dept_uid", nullable = false)
	private Department department;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "team_uid", nullable = false)
	private Team team;

	@Column(name = "org_start_date")
	private LocalDate orgStartDate;

	@Column(name = "org_end_date")
	private LocalDate orgEndDate;

	@Column(name = "org_inviter")
	private String orgInviter;

	@Column(name = "org_status")
	private boolean orgStatus;

	@Builder
	public Org(Company company, Profile profile, Department department, Team team, LocalDate orgStartDate,
		LocalDate orgEndDate, String orgInviter, boolean orgStatus) {
		this.company = company;
		this.department = department;
		this.team = team;
		this.profile = profile;
		this.orgStartDate = orgStartDate;
		this.orgEndDate = orgEndDate;
		this.orgInviter = orgInviter;
		this.orgStatus = orgStatus;

	}


	public static Org joinDefaultOrg(Company company, Profile profile, Department department, Team team,
		boolean orgStatus) {
		return builder()

			.company(company)
			.profile(profile)
			.department(department)
			.team(team)
			.orgStatus(orgStatus)
			.build();


	}

	@Override
	public String toString() {

		return "소속{" +
			"내 uuid=" + id +
			", 소속 할당 담당자='" + orgInviter + '\'' +
			", 현재 소속 여부=" + orgStatus +
			'}';

	}

}

