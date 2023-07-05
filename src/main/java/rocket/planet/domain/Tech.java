package rocket.planet.domain;

import static lombok.AccessLevel.*;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Tech {
	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "tech_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@OneToMany
	@JoinColumn(name = "tech_uid", nullable = false)
	private List<ProfileTech> profileTech;

	@Column(nullable = false, unique = true)
	private String techName;

	@Column(nullable = false)
	private String techCategory;

	@Override
	public String toString() {
		return "기술{" +
			"기술 uuid=" + id +
			", 기술 이름='" + techName + '\'' +
			", 기술 카테고리='" + techCategory + '\'' +
			'}';
	}
}
