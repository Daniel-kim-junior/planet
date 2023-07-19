package rocket.planet.service.admin;

import static rocket.planet.dto.admin.AdminDto.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import rocket.planet.domain.Profile;
import rocket.planet.domain.UserProject;
import rocket.planet.dto.common.ListReqDto;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.UserPjtRepository;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.util.common.PagingUtil;

@Service
@RequiredArgsConstructor
public class AdminSearchService {
	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;
	private final UserPjtRepository userPjtRepository;

	@Transactional
	public AdminMemberOrgListDto searchOrgUser(ListReqDto listReqDto, String userNickName) {
		Pageable pageable = PageRequest.of(listReqDto.getPage(), listReqDto.getPageSize());
		List<AdminMemberOrgDto> teamMemberList = new ArrayList<>();

		// nickname이 포함된 유저 검색
		List<Profile> userList = profileRepository.findByUserNickNameContains(userNickName);

		for (Profile user : userList) {

			// 현재 진행 중인 프로젝트 유무
			List<UserProject> projectList = userPjtRepository.findByProfile(user);
			boolean hasProject = projectList.stream()
				.anyMatch(project -> !project.getUserPjtCloseDt().isEqual(LocalDate.of(2999, 12, 31)));

			// 사용자 build
			teamMemberList.add(AdminMemberOrgDto.builder()
				.userNickName(user.getUserNickName())
				.userEmail(userRepository.findByProfile_Id(user.getId()).getUserId())
				.profileStartDt(user.getProfileStartDate())
				.userCareer(user.getProfileCareer())
				.isActive(hasProject)
				.deptName(user.getOrg().get(0).getDepartment().getDeptName())
				.teamName(user.getOrg().get(0).getTeam().getTeamName()).build());

		}

		Page<AdminMemberOrgDto> memberList = getOrgPagedResult(teamMemberList, pageable);

		PagingUtil pagingUtil = new PagingUtil(memberList.getTotalElements(),
			memberList.getTotalPages(), listReqDto.getPage(), listReqDto.getPageSize());

		return
			AdminMemberOrgListDto.builder().memberList(teamMemberList).pagingUtil(pagingUtil).build();
	}

	public Page<AdminMemberOrgDto> getOrgPagedResult(List<AdminMemberOrgDto> teamMembers, Pageable pageable) {
		int start = (int)pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), teamMembers.size());
		return new PageImpl<>(teamMembers.subList(start, end), pageable, teamMembers.size());
	}

	public AdminAuthMemberListDto searchAuthUser(ListReqDto listReqDto, String userNickName) {
		Pageable pageable = PageRequest.of(listReqDto.getPage(), listReqDto.getPageSize());
		List<AdminAuthMemberDto> teamMemberList = new ArrayList<>();

		// 닉네임이 포함됨 userList
		List<Profile> userList = profileRepository.findByUserNickNameContains(userNickName);

		for (Profile user : userList) {
			// 현재 진행 중인 프로젝트 유무
			List<UserProject> projectList = userPjtRepository.findByProfile(user);
			boolean hasProject = projectList.stream()
				.anyMatch(project -> !project.getUserPjtCloseDt().isEqual(LocalDate.of(2999, 12, 31)));

			// user build & 멤버 List에 추가
			teamMemberList.add(AdminAuthMemberDto.builder()
				.userNickName(user.getUserNickName())
				.role(String.valueOf(user.getRole()))
				.profileStartDt(user.getProfileStartDate())
				.isActive(hasProject)
				.deptName(user.getOrg().get(0).getDepartment().getDeptName())
				.teamName(user.getOrg().get(0).getTeam().getTeamName()).build());

		}
		Page<AdminAuthMemberDto> memberList = getAuthPagedResult(teamMemberList, pageable);

		PagingUtil pagingUtil = new PagingUtil(memberList.getTotalElements(),
			memberList.getTotalPages(), listReqDto.getPage(), listReqDto.getPageSize());

		return
			AdminAuthMemberListDto.builder().adminAuthMemberList(teamMemberList).pagingUtil(pagingUtil).build();

	}

	public Page<AdminAuthMemberDto> getAuthPagedResult(List<AdminAuthMemberDto> teamMembers, Pageable pageable) {
		int start = (int)pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), teamMembers.size());
		return new PageImpl<>(teamMembers.subList(start, end), pageable, teamMembers.size());
	}
}
