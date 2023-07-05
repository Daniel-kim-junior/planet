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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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
@Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = {"user_uid", "dept_uid", "team_uid", "company_uid"})
})
public class Org extends BaseTime {
	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "org_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "user_uid", nullable = false, columnDefinition = "BINARY(16)")
	private User user;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "company_uid")
	private Company company;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "dept_uid", nullable = false)
	private Department department;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "team_uid", nullable = false)
	private Team team;
	@Column(name = "belong_inviter")
	private String belongInviter;

	@Column(name = "belong_status")
	private boolean belongStatus;

	@Override
	public String toString() {
		return "소속{" +
			"내 uuid=" + id +
			", 부서 uuid=" + department +
			", 팀 uuid=" + team +
			", 소속 할당 담당자='" + belongInviter + '\'' +
			", 현재 소속 여부=" + belongStatus +
			'}';
	}

}
