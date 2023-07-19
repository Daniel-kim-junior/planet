package rocket.planet.domain;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rocket.planet.dto.profile.ProfileDto;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class UserProject {
	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "user_pjt_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "profile_uid", updatable = false, nullable = false)
	private Profile profile;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "project_uid", updatable = false, nullable = false)
	private Project project;

	@Column(nullable = false)
	private String userPjtInviter;

	@Column(nullable = false)
	@CreatedDate
	private LocalDate userPjtJoinDt;

	@Column(nullable = false)
	@LastModifiedDate
	private LocalDate userPjtModifyDt;

	@Column(nullable = false)
	private LocalDate userPjtCloseDt;

	@Column(nullable = false)
	private boolean userPjtCloseApply;

	@Column
	private String userPjtDesc;

	@Builder
	public UserProject(Profile profile, Project project, String userPjtInviter, LocalDate userPjtJoinDt,
		LocalDate userPjtModifyDt, LocalDate userPjtCloseDt, boolean userPjtCloseApply, String userPjtDesc) {
		this.profile = profile;
		this.project = project;
		this.userPjtInviter = userPjtInviter;
		this.userPjtJoinDt = userPjtJoinDt;
		this.userPjtModifyDt = userPjtModifyDt;
		this.userPjtCloseDt = userPjtCloseDt;
		this.userPjtCloseApply = userPjtCloseApply;
		this.userPjtDesc = userPjtDesc;
	}

	@Override
	public String toString() {
		return "유저의 프로젝트{" +
			"유저 프로젝트 투입 담당자='" + userPjtInviter + '\'' +
			", 유저 프로젝트 조인 일자=" + userPjtJoinDt +
			", 유저 프로젝트 상세정보 수정일=" + userPjtModifyDt +
			", 유저 프로젝트 마감일=" + userPjtCloseDt +
			", 유저 프로젝트 마감 신청 여부=" + userPjtCloseApply +
			", 유저 프로젝트 상세정보 설명='" + userPjtDesc + '\'' +
			'}';
	}

	public void approveProjectClose() {
		this.userPjtCloseApply = false;
		this.userPjtCloseDt = LocalDate.now();
	}

	public void rejectProjectClose() {
		this.userPjtCloseApply = false;
	}

	public void requestProjectClose() {
		this.userPjtCloseApply = true;
	}

	public void updateUserPjtDesc(ProfileDto.insideProjectUpdateReqDto insidePjtReqDto) {
		this.userPjtDesc = insidePjtReqDto.getProjectDesc();
	}

}
