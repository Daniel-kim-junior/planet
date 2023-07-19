package rocket.planet.domain;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rocket.planet.dto.admin.AdminDeptTeamDto;
import rocket.planet.dto.admin.AdminDeptTeamDto.AdminTeamAddReqDto;
import rocket.planet.repository.jpa.OrgRepository;
import rocket.planet.repository.jpa.TeamRepository;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Team extends BaseTime {

    @Id
    @GeneratedValue(generator = "uuid4")
    @GenericGenerator(name = "UUID", strategy = "uuid4")
    @Column(name = "team_uid", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(optional = false, fetch = LAZY)
    @JoinColumn(name = "dept_uid", nullable = false, updatable = false)
    private Department department;

    @OneToMany(mappedBy = "team")
    private List<Project> project = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<Org> org = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private String teamName;

    @Column(nullable = false)
    private String teamDesc;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrgType teamType;

    @Column
    private boolean teamInactive;


    @Builder
    public Team(Department department, String teamName, String teamDesc, OrgType teamType, boolean teamInactive) {
        this.department = department;
        this.teamName = teamName;
        this.teamDesc = teamDesc;
        this.teamType = teamType;
        this.teamInactive = teamInactive;
    }

    public static Team defaultTeam(AdminTeamAddReqDto dto, OrgType orgType, Department department) {
        return builder()
                .department(department)
                .teamName(dto.getTeamName())
                .teamDesc(dto.getTeamDesc())
                .teamType(orgType)
                .teamInactive(false)
                .build();
    }

    public Team modifyTeam(String teamName, String teamDesc) {
        this.teamName = teamName;
        this.teamDesc = teamDesc;
        return this;
    }

    public static Team addTeam(String teamName, String teamDesc, OrgType teamType, Department department) {
        return builder()
                .department(department)
                .teamName(teamName)
                .teamDesc(teamDesc)
                .teamType(teamType)
                .build();
    }

    @Override
    public String toString() {
        return "팀{" +
                "팀 uuid=" + id +
                ", 팀 이름='" + teamName + '\'' +
                ", 팀 설명='" + teamDesc + '\'' +
                ", 팀 타입='" + teamType + '\'' +
                '}';
    }


public void updateTeamInactive() {
    this.teamInactive = true;
}
}
