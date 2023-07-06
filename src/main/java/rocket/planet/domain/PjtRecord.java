package rocket.planet.domain;

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
@NoArgsConstructor(access = PROTECTED)
@Table(name = "profile_pjt_records")
public class PjtRecord {
	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "profile_pjt_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "profile_uid", nullable = false)
	private Profile profile;

	@Column(name = "profile_pjt_name", nullable = false, unique = true)
	private String pjtName;

	@Column(name = "profile_pjt_desc", nullable = false)
	private String pjtDesc;

	@Column(name = "profile_pjt_start_dt", nullable = false)
	private LocalDate pjtStartDt;

	@Column(name = "profile_pjt_end_dt", nullable = false)
	private LocalDate pjtEndDt;

	@Column(name = "profile_pjt_tech", nullable = false)
	private String pjtTech;
	
	@Column(name = "profile_pjt_user_tech", nullable = false)
	private String pjtUserTech;

	@Builder
	public PjtRecord(Profile profile, String pjtName, String pjtDesc, LocalDate pjtStartDt, LocalDate pjtEndDt,
		String pjtTech, String pjtUserTech) {
		this.profile = profile;
		this.pjtName = pjtName;
		this.pjtDesc = pjtDesc;
		this.pjtStartDt = pjtStartDt;
		this.pjtEndDt = pjtEndDt;
		this.pjtTech = pjtTech;
		this.pjtUserTech = pjtUserTech;
	}

	@Override
	public String toString() {
		return "프로젝트 사외 이력{" +
			"프로필 uuid=" + id +
			", 프로젝트 이름='" + pjtName + '\'' +
			", 프로젝트 설명='" + pjtDesc + '\'' +
			", 프로젝트 시작일=" + pjtStartDt +
			", 프로젝트 종료일=" + pjtEndDt +
			", 프로젝트 사용 기술='" + pjtTech + '\'' +
			", 프로젝트에서 쓴 나의 기술='" + pjtUserTech + '\'' +
			'}';
	}
}
