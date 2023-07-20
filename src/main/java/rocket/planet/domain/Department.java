package rocket.planet.domain;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;
import static rocket.planet.dto.admin.AdminDeptTeamDto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Department extends BaseTime {

	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "dept_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(optional = false, fetch = LAZY)
	@JoinColumn(name = "company_uid", nullable = false, updatable = false)
	private Company company;

	@OneToMany(mappedBy = "department")
	private List<Team> team = new ArrayList<>();

	@OneToMany(mappedBy = "department")
	private List<Org> org = new ArrayList<>();

	@Column(nullable = false, unique = true)
	private String deptName;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrgType deptType;

	@Column
	private boolean deptInactive;

	@Builder
	public Department(Company company, String deptName, OrgType deptType, boolean deptInactive) {
		this.company = company;
		this.deptName = deptName;
		this.deptType = deptType;
		this.deptInactive = deptInactive;
	}

	public void addTeam(Team team) {
		this.team.add(team);
	}

	public static Department defaultDept(AdminDeptAddReqDto dto, Company company) {
		return builder()
			.deptName(dto.getName())
			.company(company)
			.deptType(OrgType.valueOf(dto.getDeptType()))
			.deptInactive(false)
			.build();
	}

	public Department update(String deptName) {
		this.deptName = deptName;
		return this;
	}

	@Override
	public String toString() {
		return "Department{" +
			"부서 uuid=" + id +
			", 부서 이름='" + deptName + '\'' +
			", 개발/비개발='" + deptType + '\'' +
			'}';
	}

	public void updateDeptInactive() {
		this.deptInactive = true;
	}

}
