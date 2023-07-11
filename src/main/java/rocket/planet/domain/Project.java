package rocket.planet.domain;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;
import static rocket.planet.dto.project.ProjectUpdateDto.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rocket.planet.dto.project.ProjectRegisterResDto;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Project {
	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "project_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "team_uid")
	private Team team;

	@OneToMany(mappedBy = "project")
	private List<UserProject> userProject = new ArrayList<>();

	@Column(nullable = false, unique = true)
	private String projectName;

	@Column(nullable = false)
	private String projectDesc;

	@Column(nullable = false)
	private LocalDate projectStartDt;

	@Column(nullable = false)
	private LocalDate projectEndDt;

	@Column(nullable = false)
	private String projectTech;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ProjectStatus projectStatus;

	@Column(nullable = false)
	private String projectLastModifiedBy;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrgType projectType;

	@Column(nullable = false)
	@LastModifiedDate
	private LocalDate lastModifiedDate;

	@Builder
	public Project(Team team, String projectName, String projectDesc, LocalDate projectStartDt,
		LocalDate projectEndDt, String projectTech, ProjectStatus projectStatus, String projectLastModifiedBy,
		OrgType projectType, LocalDate lastModifiedDate) {
		this.team = team;
		this.projectName = projectName;
		this.projectDesc = projectDesc;
		this.projectStartDt = projectStartDt;
		this.projectEndDt = projectEndDt;
		this.projectTech = projectTech;
		this.projectStatus = projectStatus;
		this.projectLastModifiedBy = projectLastModifiedBy;
		this.projectType = projectType;
		this.lastModifiedDate = lastModifiedDate;
	}

	@Override
	public String toString() {
		return "프로젝트{" +
			"프로젝트 uuid=" + id +
			", 프로젝트 이름='" + projectName + '\'' +
			", 프로젝트 사용 기술='" + projectTech + '\'' +
			", 프로젝트 설명='" + projectDesc + '\'' +
			", 프로젝트 시작일=" + projectStartDt +
			", 프로젝트 종료일=" + projectEndDt +
			", 프로젝트 상태=" + projectStatus +
			", 프로젝트 마지막 수정자='" + projectLastModifiedBy + '\'' +
			", 프로젝트 타입(개발/비개발)='" + projectType + '\'' +
			", 마지막 수정일자=" + lastModifiedDate +
			"}";
	}

	public void toUpdateProjectStatusDto(Project project) {
		ProjectRegisterResDto.builder()
			.projectName(project.getProjectName())
			.projectStatus(ProjectStatus.ONGOING)
			.build();
	}

	public void updateProject(ProjectUpdateDetailDto project) {
		this.projectDesc = project.getProjectDesc();
		this.projectStartDt = project.getProjectStartDt();
		this.projectEndDt = project.getProjectEndDt();
		this.projectTech = project.getProjectTech();
		this.projectLastModifiedBy = project.getUserNickName();
	}

	public void deleteProject(ProjectUpdateStatusDto projectDeleteDto) {
		this.projectStatus = ProjectStatus.DELETED;
		this.projectLastModifiedBy = projectDeleteDto.getUserNickName();
	}

}
