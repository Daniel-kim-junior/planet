package rocket.planet.domain;

import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class Company {

	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "company_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(nullable = false, unique = true)
	private String companyName;

	@OneToMany(mappedBy = "company")
	private List<Department> department = new ArrayList<>();

	@OneToMany(mappedBy = "company")
	private List<Org> org = new ArrayList<>();

	@Builder
	public Company(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public String toString() {
		return "Company{" +
			"회사 uuid=" + id +
			", 회사 이름='" + companyName + '\'' +
			'}';
	}
}
