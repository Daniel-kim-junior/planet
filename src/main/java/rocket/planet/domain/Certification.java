package rocket.planet.domain;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "profile_cert")
@NoArgsConstructor(access = PROTECTED)
public class Certification {
	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "cert_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(optional = false, fetch = LAZY)
	@JoinColumn(columnDefinition = "BINARY(16)", name = "profile_uid", nullable = false)
	private Profile profile;

	@Column(nullable = false)
	private String certName;

	@Column(nullable = false)
	private LocalDate certDt;

	@Column(nullable = false)
	private String certAgency;

	@Column
	private LocalDate certExpireDate;

	@Column(nullable = false)
	private String certType;

	@Column(nullable = false, unique = true)
	private String certNumber;

	@Builder
	public Certification(Profile profile, String certName, LocalDate certDt, String certAgency,
		LocalDate certExpireDate,
		String certType, String certNumber) {
		this.profile = profile;
		this.certName = certName;
		this.certDt = certDt;
		this.certAgency = certAgency;
		this.certExpireDate = certExpireDate;
		this.certType = certType;
		this.certNumber = certNumber;
	}

	@Override
	public String toString() {
		return "자격증{" + "자격증 uuid=" + id + ", 자격증 이름='" + certName + '\'' + ", 자격증 취득일=" + certDt
			+ ", 자격증 발급기관='" + certAgency + '\'' + ", 만료일='" + certExpireDate + '\'' + ", 자격증 유형='"
			+ certType + '\'' + ", 자격증 번호='" + certNumber + '\'' + '}';
	}

}
