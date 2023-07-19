package rocket.planet.service.admin;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.domain.Profile;
import rocket.planet.domain.User;
import rocket.planet.dto.admin.AdminDto.AdminAuthModifyReqDto;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.service.auth.AuthorityService;

@Service
public class AdminUserService {
	private ProfileRepository profileRepository;
	private UserRepository userRepository;

	private AuthorityService authorityService;

	@Transactional
	public void disabledUser(String userNickName) {
		Optional<Profile> user = profileRepository.findByUserNickName(userNickName);

		Optional<User> retiredUser = userRepository.findByProfile(user.get());

		// user lock & 퇴사일 & 비밀번호/비밀번호 변경일/생성일 삭제
		retiredUser.get().updateRetiredUser();

		// 소속/role 삭제, 권한 삭제, 프로필 권한 삭제
		authorityService.modifyAuthority(
			AdminAuthModifyReqDto.builder()
				.teamName(user.get().getOrg().get(0).getTeam().getTeamName())
				.deptName(user.get().getOrg().get(0).getDepartment().getDeptName())
				.userNickName(userNickName)
				.role("GUEST").build());

		user.get().updateRetiredProfile();

	}
}
