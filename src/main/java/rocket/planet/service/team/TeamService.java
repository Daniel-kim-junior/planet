package rocket.planet.service.team;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.domain.Org;
import rocket.planet.domain.Profile;
import rocket.planet.domain.UserProject;
import rocket.planet.dto.team.TeamMemberResDto;
import rocket.planet.repository.jpa.OrgRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.UserPjtRepository;
import rocket.planet.repository.jpa.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {

	private final ProfileRepository profileRepository;
	private final OrgRepository orgRepository;
	private final UserRepository userRepository;
	private final UserPjtRepository userPjtRepository;

	public List<TeamMemberResDto> getMemberList(String teamName) {
		List<TeamMemberResDto> teamMemberList = new ArrayList<>();

		// 팀 이름으로 소속 찾기 -> 소속으로 팀원 프로필 목록 조회
		Optional<Org> organization = orgRepository.findAllByTeam_TeamName(teamName).stream().findFirst();
		log.info("organization==========> {}", organization);
		List<Profile> teamMemberProfileList = profileRepository.findByOrg(organization);
		log.info("teamMemberProfileList==========> {}", teamMemberProfileList);

		// 팀원 프로필로 현재 진행 중인 프로젝트 존재 여부 찾기
		for (Profile teamMember : teamMemberProfileList) {
			List<UserProject> projectList = userPjtRepository.findAllByProfile(teamMember);

			// 프로젝트 마감 일자가 있으면 hasProject == true
			boolean hasProject = projectList.stream()
				.anyMatch(project -> !project.getUserPjtCloseDt().isEqual(LocalDate.of(2999, 12, 31)));

			TeamMemberResDto teamMemberDto = TeamMemberResDto.builder()
				.userNickName(teamMember.getUserNickName())
				.profileEmail(userRepository.findByProfile_Id(teamMember.getId()).getUserId())
				.profileCareer(teamMember.getProfileCareer())
				.profileStart(teamMember.getProfileStartDate())
				.isActive(hasProject)
				.build();

			teamMemberList.add(teamMemberDto);

		}
		return teamMemberList;

	}
}
