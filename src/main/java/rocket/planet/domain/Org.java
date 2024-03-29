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
public class Org extends BaseTime {

	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "org_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "profile_uid", nullable = false)
	private Profile profile;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "company_uid")
	private Company company;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "dept_uid")
	private Department department;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "team_uid")
	private Team team;

	private boolean orgStatus;

	private String orgInviter;

	@Builder
	public Org(Company company, Profile profile, Department department, Team team, String orgInviter,
		boolean orgStatus) {
		this.company = company;
		this.department = department;
		this.team = team;
		this.profile = profile;
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

	public void hasNoTeam() {
		this.team = null;
	}

	public void hasNoDept() {
		this.department = null;
	}

	@Override
	public String toString() {
		return "Org{" +
			"id=" + id +
			", profile=" + profile +
			", company=" + company +
			", department=" + department +
			", team=" + team +
			", orgInviter='" + orgInviter + '\'' +
			", orgStatus=" + orgStatus +
			'}';
	}

	public void updateOrg(Team newTeam, Department newDept) {
		this.team = newTeam;
		this.department = newDept;
	}
}

