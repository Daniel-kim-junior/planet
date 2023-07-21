package rocket.planet.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import rocket.planet.domain.Profile;
import rocket.planet.domain.User;
import rocket.planet.dto.admin.AdminDto.AdminAuthModifyReqDto;
import rocket.planet.dto.common.CommonResDto;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.service.auth.AuthorityService;
import rocket.planet.util.exception.NoUserNickNameException;

@Service
@RequiredArgsConstructor
public class AdminUserService {
	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;

	private final AuthorityService authorityService;

	@Transactional
	public CommonResDto disabledUser(String userNickName) {

		Profile user = profileRepository.findByUserNickName(userNickName).orElseThrow(NoUserNickNameException::new);
		User retiredUser = userRepository.findByProfile(user);

		// user lock & 퇴사일 & 비밀번호/비밀번호 변경일/생성일 삭제
		retiredUser.updateRetiredUser();

		// 소속/role 삭제, 권한 삭제, 프로필 권한 삭제
		authorityService.modifyAuthority(
			AdminAuthModifyReqDto.builder()
				.teamName(user.getOrg().get(0).getTeam().getTeamName())
				.deptName(user.getOrg().get(0).getDepartment().getDeptName())
				.userNickName(userNickName)
				.role("GUEST").build());

		user.updateRetiredProfile();

		return CommonResDto.builder().message(userNickName + "를 퇴사자로 변경하였습니다.").build();

	}
}
