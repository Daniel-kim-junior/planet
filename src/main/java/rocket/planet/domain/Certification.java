package rocket.planet.domain;

import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "profile_cert")
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Certification {
	@Id
	private UUID id;

	@ManyToOne(optional = false)
	@JoinColumn(columnDefinition = "BINARY(16)", name = "profile_uid", nullable = false)
	@MapsId
	private Profile profile;

	@Column(nullable = false, unique = true)
	private String certName;

	@Column(nullable = false)
	private LocalDate certDt;

	@Column(nullable = false)
	private String certAgency;

	@Column(nullable = false)
	private String certExpireDate;

	@Column(nullable = false)
	private String certType;

	@Override
	public String toString() {
		return "자격증{" + "자격증 uuid=" + id + ", 자격증 이름='" + certName + '\'' + ", 자격증 취득일=" + certDt
			+ ", 자격증 발급기관='" + certAgency + '\'' + ", 만료일='" + certExpireDate + '\'' + ", 자격증 유형='"
			+ certType + '\'' + '}';
	}

}
