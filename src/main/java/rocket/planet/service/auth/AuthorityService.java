package rocket.planet.service.auth;

import static rocket.planet.dto.admin.AdminDto.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.domain.AuthType;
import rocket.planet.domain.Authority;
import rocket.planet.domain.Department;
import rocket.planet.domain.Org;
import rocket.planet.domain.Profile;
import rocket.planet.domain.ProfileAuthority;
import rocket.planet.domain.Team;
import rocket.planet.domain.UserProject;
import rocket.planet.repository.jpa.AuthRepository;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.OrgRepository;
import rocket.planet.repository.jpa.PfAuthRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.TeamRepository;
import rocket.planet.repository.jpa.UserPjtRepository;
import rocket.planet.repository.jpa.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorityService {
	private final AuthRepository authRepository;
	private final UserRepository userRepository;
	private final ProfileRepository profileRepository;
	private final PfAuthRepository pfAuthRepository;
	private final OrgRepository orgRepository;
	private final UserPjtRepository userPjtRepository;
	private final DeptRepository deptRepository;
	private final TeamRepository teamRepository;

	final String authorizerEmail = "@gmail.com";

	public ProfileAuthority addAuthority(AdminAddAuthDto adminAddAuthDto) {
		Optional<Profile> projectLeaderProfile = profileRepository.findByUserNickName(
			adminAddAuthDto.getAuthNickName());
		UUID adminProfileId = (profileRepository.findByUserNickName(adminAddAuthDto.getAuthorizerNickName())
			.get()).getId();
		UUID userId = userRepository.findByProfile_Id(adminProfileId).getId();

		log.info("projectLeaderProfile {} => ", projectLeaderProfile);
		Authority newAuth = authRepository.save(Authority.builder()
			.authorizerId(adminAddAuthDto.getAuthorizerNickName() + authorizerEmail)
			.authType(adminAddAuthDto.getAuthType())
			.authTargetId(adminAddAuthDto.getAuthTargetId())
			.userUid(userId)
			.build());

		return addProfileAuthority(newAuth, projectLeaderProfile.get());
	}

	public ProfileAuthority addProfileAuthority(Authority authority, Profile profile) {
		return pfAuthRepository.save(ProfileAuthority.builder()
			.authority(authority)
			.profile(profile)
			.build());
	}

	@Transactional
	public void modifyAuthority(AdminAuthModifyReqDto adminAuthModifyReqDto) {
		Department department = deptRepository.findByDeptName(adminAuthModifyReqDto.getDeptName());
		Team team = teamRepository.findByTeamName(adminAuthModifyReqDto.getTeamName());

		// 1. 프로필에서 역할 수정
		Profile user = profileRepository.findByUserNickName(adminAuthModifyReqDto.getUserNickName()).orElseThrow();
		user.updateRole(adminAuthModifyReqDto.getRole());

		System.out.println(user + "------------");
		// 2. 프로필-권한에서 권한 삭제
		// user가 갖고 있는 프로필-권한의 아이디가 팀이나 부문일 경우, 프로필-권한 & 권한 삭제
		if (pfAuthRepository.findByProfile(user).getAuthTargetId().equals(department.getId())
			|| pfAuthRepository.findByProfile(user).getAuthTargetId().equals(team.getId())) {
			pfAuthRepository.deleteByAuthorityAndProfile(pfAuthRepository.findByProfile(user), user);
			authRepository.deleteById(pfAuthRepository.findByProfile(user).getId());
		}

		// 3. 권한 추가 & 4. 프로필-권한 추가
		if (adminAuthModifyReqDto.getRole().equals("PILOT")) {
			addAuthority(AdminAddAuthDto.builder()
				.authNickName(user.getUserNickName())
				.authorizerNickName("admin" + authorizerEmail)
				.authType(AuthType.TEAM)
				.authTargetId(team.getId())
				.build());
		} else if (adminAuthModifyReqDto.getRole().equals("CAPTAIN")) {
			addAuthority(AdminAddAuthDto.builder()
				.authNickName(user.getUserNickName())
				.authorizerNickName("admin" + authorizerEmail)
				.authType(AuthType.DEPARTMENT)
				.authTargetId(department.getId())
				.build());
		}

	}

	@Transactional
	public List<AdminAuthMemberResDto> getTeamMemberList(String teamName) {
		List<AdminAuthMemberResDto> teamMemberList = new ArrayList<>();

		// 팀으로 소속 인원 찾기
		Optional<Org> organization = orgRepository.findAllByTeam_TeamName(teamName).stream().findFirst();
		List<Profile> profileList = profileRepository.findByOrg(organization);

		for (Profile profile : profileList) {

			// 현재 진행중인 프로젝트 유무 확인
			List<UserProject> projectList = userPjtRepository.findAllByProfile(profile);
			boolean isActive = projectList.stream()
				.anyMatch(project -> !project.getUserPjtCloseDt().isEqual(LocalDate.of(2999, 12, 31)));

			AdminAuthMemberResDto member = AdminAuthMemberResDto.builder()
				.userNickName(profile.getUserNickName())
				.deptName(organization.get().getDepartment().getDeptName())
				.teamName(organization.get().getTeam().getTeamName())
				.profileStartDt(profile.getProfileStartDate())
				.role(String.valueOf(profile.getRole()))
				.isActive(isActive)
				.build();

			teamMemberList.add(member);
		}

		return teamMemberList;

	}
}
