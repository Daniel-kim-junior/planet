package rocket.planet.domain;

import static lombok.AccessLevel.*;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
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
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Company {

	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "company_uid", columnDefinition = "BINARY(16)")
	private UUID id;
	@Column(nullable = false, unique = true)
	private String companyName;

	@OneToMany(mappedBy = "company")
	private List<Department> department;

	@Override
	public String toString() {
		return "Company{" +
			"회사 uuid=" + id +
			", 회사 이름='" + companyName + '\'' +
			'}';
	}
}
