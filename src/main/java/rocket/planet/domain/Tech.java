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
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Tech {
	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "tech_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@OneToMany(mappedBy = "tech")
	private List<ProfileTech> profileTech = new ArrayList<>();

	@Column(nullable = false, unique = true)
	private String techName;

	@Column(nullable = false)
	private String techCategory;

	@Builder
	public Tech(String techName, String techCategory) {
		this.techName = techName;
		this.techCategory = techCategory;
	}

	@Override
	public String toString() {
		return "기술{" +
			"기술 uuid=" + id +
			", 기술 이름='" + techName + '\'' +
			", 기술 카테고리='" + techCategory + '\'' +
			'}';
	}
}
