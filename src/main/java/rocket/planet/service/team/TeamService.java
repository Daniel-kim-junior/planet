package rocket.planet.service.team;

import static rocket.planet.dto.admin.AdminDto.*;
import static rocket.planet.dto.team.TeamMemberDto.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.domain.Org;
import rocket.planet.domain.Profile;
import rocket.planet.domain.Role;
import rocket.planet.domain.UserProject;
import rocket.planet.dto.common.ListReqDto;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.OrgRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.TeamRepository;
import rocket.planet.repository.jpa.UserPjtRepository;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.service.auth.AuthorityService;
import rocket.planet.util.common.PagingUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {

	private final ProfileRepository profileRepository;
	private final OrgRepository orgRepository;
	private final UserRepository userRepository;
	private final UserPjtRepository userPjtRepository;
	private final TeamRepository teamRepository;
	private final DeptRepository deptRepository;
	private final AuthorityService authorityService;

	@Transactional
	public TeamMemberListDto getMemberList(ListReqDto listReqDto, String teamName) {
		Pageable pageable = PageRequest.of(listReqDto.getPage(), listReqDto.getPageSize());

		List<TeamMemberInfoDto> teamMemberList = new ArrayList<>();

		// 팀 이름으로 소속 찾기 -> 소속으로 팀원 프로필 목록 조회
		List<Org> organization = orgRepository.findAllByTeam_TeamName(teamName);

		for (Org org : organization) {
			Profile profile = profileRepository.findByOrg(Optional.ofNullable(org));

			// 팀원 프로필로 현재 진행 중인 프로젝트 존재 여부 찾기
			List<UserProject> projectList = userPjtRepository.findAllByProfile(profile);

			// 프로젝트 마감 일자가 있으면 hasProject == true
			boolean hasProject = projectList.stream()
				.anyMatch(project -> !project.getUserPjtCloseDt().isEqual(LocalDate.of(2999, 12, 31)));

			TeamMemberInfoDto teamMemberDto = TeamMemberInfoDto.builder()
				.userNickName(profile.getUserNickName())
				.profileEmail(userRepository.findByProfile_Id(profile.getId()).getUserId())
				.profileCareer(profile.getProfileCareer())
				.profileStart(profile.getProfileStartDate())
				.isActive(hasProject)
				.deptName(organization.get(0).getDepartment().getDeptName())
				.teamName(organization.get(0).getTeam().getTeamName())
				.build();

			teamMemberList.add(teamMemberDto);

		}

		Page<TeamMemberInfoDto> memberList = getPagedResult(teamMemberList, pageable);

		PagingUtil pagingUtil = new PagingUtil(memberList.getTotalElements(),
			memberList.getTotalPages(), listReqDto.getPage(), listReqDto.getPageSize());

		return
			TeamMemberListDto.builder().memberList(teamMemberList).pagingUtil(pagingUtil).build();

	}

	public Page<TeamMemberInfoDto> getPagedResult(List<TeamMemberInfoDto> teamMembers, Pageable pageable) {
		int start = (int)pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), teamMembers.size());
		return new PageImpl<>(teamMembers.subList(start, end), pageable, teamMembers.size());
	}

	@Transactional
	public void modifyMemberOrg(AdminOrgModifyReqDto orgModifyReqList) {

		// 소속 변경
		Optional<Profile> user = profileRepository.findByUserNickName(orgModifyReqList.getUserNickName());

		Optional<Org> oldOrg = orgRepository.findById(user.get().getOrg().get(0).getId());

		// 권한 삭제
		if (!user.get().getRole().equals(Role.CREW) && !user.get().getRole().equals(Role.PL)) {
			authorityService.modifyAuthority(AdminAuthModifyReqDto.builder()
				.userNickName(user.get().getUserNickName())
				.role("CREW")
				.deptName(orgModifyReqList.getDeptName())
				.teamName(orgModifyReqList.getTeamName())
				.build());
		}

		oldOrg.get()
			.updateOrg(teamRepository.findByTeamName(orgModifyReqList.getTeamName()),
				deptRepository.findByDeptName(orgModifyReqList.getDeptName()));

	}
}
