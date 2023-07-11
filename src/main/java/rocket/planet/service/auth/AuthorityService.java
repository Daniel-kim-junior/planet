package rocket.planet.service.auth;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.domain.AuthType;
import rocket.planet.domain.Authority;
import rocket.planet.domain.Profile;
import rocket.planet.domain.ProfileAuthority;
import rocket.planet.repository.jpa.AuthRepository;
import rocket.planet.repository.jpa.PfAuthRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorityService {
	private final AuthRepository authRepository;
	private final UserRepository userRepository;
	private final ProfileRepository profileRepository;
	private final PfAuthRepository pfAuthRepository;

	final String authorizerEmail = "@gmail.com";

	public ProfileAuthority addAuthority(UUID authTargetId, AuthType authType, String userNickName,
		String projectLeader) {
		Optional<Profile> projectLeaderProfile = profileRepository.findByUserNickName(projectLeader);
		UUID adminProfileId = (profileRepository.findByUserNickName(userNickName).get()).getId();
		UUID userId = userRepository.findByProfile_Id(adminProfileId).getId();

		log.info("projectLeaderProfile {} => ", projectLeaderProfile);
		Authority newAuth = authRepository.save(Authority.builder()
			.authorizerId(userNickName + authorizerEmail)
			.authType(authType)
			.authTargetId(authTargetId)
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
}
