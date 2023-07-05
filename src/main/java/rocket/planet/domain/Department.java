package rocket.planet.domain;

import static lombok.AccessLevel.*;

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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Department {

	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "dept_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "company_uid", nullable = false, updatable = false)
	private Company company;

	@OneToMany(mappedBy = "department")
	private List<Team> team;

	@OneToMany(mappedBy = "department")
	private List<Org> org;

	@Column(nullable = false, unique = true)
	private String deptName;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrgType deptType;

	@Override
	public String toString() {
		return "Department{" +
			"부서 uuid=" + id +
			", 부서 이름='" + deptName + '\'' +
			", 개발/비개발='" + deptType + '\'' +
			'}';
	}

}
