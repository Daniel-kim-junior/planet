package rocket.planet.domain;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@IdClass(UserProjectId.class)
@EntityListeners(AuditingEntityListener.class)
public class UserProject {
	@Id
	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "user_uid", insertable = false, updatable = false, nullable = false, columnDefinition = "BINARY(16)")
	private User user;

	@Id
	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "project_uid", insertable = false, updatable = false, nullable = false, columnDefinition = "BINARY(16)")
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
	@Column(nullable = false)
	private String userPjtDesc;

	@Override
	public String toString() {
		return "유저의 프로젝트{" +
			"유저 프로젝트 투입 담당자='" + userPjtInviter + '\'' +
			", 유저 프로젝트 조인 일자=" + userPjtJoinDt +
			", 유저 프로젝트 상세정보 수정일=" + userPjtModifyDt +
			", 유저 프로젝트 마감일=" + userPjtCloseDt +
			", 유저 프로젝트 마감 여부=" + userPjtCloseApply +
			", 유저 프로젝트 상세정보 설명='" + userPjtDesc + '\'' +
			'}';
	}
}