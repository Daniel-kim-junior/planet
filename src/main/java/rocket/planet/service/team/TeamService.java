package rocket.planet.service.team;

import static rocket.planet.dto.admin.AdminDto.*;
import static rocket.planet.dto.team.TeamMemberDto.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
import rocket.planet.dto.common.CommonResDto;
import rocket.planet.dto.common.ListReqDto;
import rocket.planet.repository.jpa.CompanyRepository;
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
	private final CompanyRepository companyRepository;

	@Transactional
	public TeamMemberListDto getMemberList(ListReqDto listReqDto, String teamName) {
		Pageable pageable = PageRequest.of(listReqDto.getPage(), listReqDto.getPageSize());

		List<TeamMemberInfoDto> teamMemberList = new ArrayList<>();

		if (teamName != null) {
			// 팀 이름으로 소속 찾기 -> 소속으로 팀원 프로필 목록 조회
			List<Org> organization = orgRepository.findAllByTeam_TeamName(teamName);

			for (Org org : organization) {
				Profile profile = profileRepository.findByOrg(Optional.ofNullable(org));
				if (profile.getRole().equals(Role.ADMIN) || profile.getRole().equals(Role.RADAR)
					|| !profile.isProfileStatus())
					continue;
				// 팀원 프로필로 현재 진행 중인 프로젝트 존재 여부 찾기
				List<UserProject> projectList = userPjtRepository.findAllByProfile(profile);

				// 프로젝트 마감 일자가 있으면 hasProject == true
				boolean hasProject = projectList.stream()
					.anyMatch(project -> !project.getUserPjtCloseDt().isEqual(LocalDate.of(2999, 12, 31)));

				String userEmail = userRepository.findByProfile_Id(profile.getId()).getUserId();
				TeamMemberInfoDto teamMemberDto = TeamMemberInfoDto.builder()
					.userNickName(profile.getUserNickName())
					.profileEmail(userEmail)
					.profileCareer(profile.getProfileCareer())
					.profileStart(profile.getProfileStartDate())
					.isActive(hasProject)
					.deptName(organization.get(0).getDepartment().getDeptName())
					.teamName(organization.get(0).getTeam().getTeamName())
					.build();

				teamMemberList.add(teamMemberDto);

			}
		} else {
			List<Profile> noTeamProfile = profileRepository.findAllByOrg(null);
			for (Profile noTeam : noTeamProfile) {
				if (noTeam.getRole().equals(Role.ADMIN) || noTeam.getRole().equals(Role.RADAR)
					|| !noTeam.isProfileStatus())
					continue;
				// 팀원 프로필로 현재 진행 중인 프로젝트 존재 여부 찾기
				List<UserProject> projectList = userPjtRepository.findAllByProfile(noTeam);

				// 프로젝트 마감 일자가 있으면 hasProject == true
				boolean hasProject = projectList.stream()
					.anyMatch(project -> !project.getUserPjtCloseDt().isEqual(LocalDate.of(2999, 12, 31)));

				String userEmail = userRepository.findByProfile_Id(noTeam.getId()).getUserId();
				TeamMemberInfoDto teamMemberDto = TeamMemberInfoDto.builder()
					.userNickName(noTeam.getUserNickName())
					.profileEmail(userEmail)
					.profileCareer(noTeam.getProfileCareer())
					.profileStart(noTeam.getProfileStartDate())
					.isActive(hasProject)
					.deptName("무소속")
					.teamName("무소속")
					.build();

				teamMemberList.add(teamMemberDto);
			}

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
	public CommonResDto modifyMemberOrg(AdminOrgModifyReqDto orgModifyReqList) {

		// 소속 변경
		Profile user = profileRepository.findByUserNickName(orgModifyReqList.getUserNickName()).orElseThrow();

		if (user.getOrg() == null) {
			// org 생성
			Org nOrg = Org.builder()
				.orgInviter("admin")
				.orgStatus(true)
				.team(teamRepository.findByTeamName(orgModifyReqList.getTeamName()))
				.company(companyRepository.findByCompanyName("dktechin"))
				.department(deptRepository.findByDeptName(orgModifyReqList.getDeptName()))
				.profile(user)
				.build();

			user.updateOrg(Collections.singletonList(nOrg));

			return CommonResDto.builder().message("사용자의 소속을 변경하였습니다.").build();
		}
		Optional<Org> oldOrg = orgRepository.findById(user.getOrg().get(0).getId());

		// 권한 삭제
		if (!user.getRole().equals(Role.CREW) && !user.getRole().equals(Role.PL)) {
			authorityService.modifyAuthority(AdminAuthModifyReqDto.builder()
				.userNickName(user.getUserNickName())
				.role("CREW")
				.deptName(orgModifyReqList.getDeptName())
				.teamName(orgModifyReqList.getTeamName())
				.build());
		}

		oldOrg.get()
			.updateOrg(
				teamRepository.findByTeamName(orgModifyReqList.getTeamName()),
				deptRepository.findByDeptName(orgModifyReqList.getDeptName()));

		return CommonResDto.builder().message("사용자의 소속을 변경하였습니다.").build();
	}

}
