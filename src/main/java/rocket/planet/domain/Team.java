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
public class Team extends BaseTime {
	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "team_uid", columnDefinition = "BINARY(16)")
	private UUID id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "dept_uid", nullable = false, updatable = false)
	private Department department;

	@OneToMany(mappedBy = "team")
	private List<Project> project;

	@OneToMany(mappedBy = "team")
	private List<Org> org;

	@Column(nullable = false)
	private String teamCode;

	@Column(nullable = false, unique = true)
	private String teamName;

	@Column(nullable = false)
	private String teamDesc;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrgType teamType;

	@Override
	public String toString() {
		return "팀{" +
			"팀 uuid=" + id +
			", 팀 코드='" + teamCode + '\'' +
			", 팀 이름='" + teamName + '\'' +
			", 팀 설명='" + teamDesc + '\'' +
			", 팀 타입='" + teamType + '\'' +
			'}';
	}

}
