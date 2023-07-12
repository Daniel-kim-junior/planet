package rocket.planet.service.profile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import rocket.planet.domain.redis.ProfileVisitor;
import rocket.planet.dto.visitor.ProfileVisitorListResDto;
import rocket.planet.repository.redis.ProfileVisitorRepository;
import rocket.planet.util.exception.NoSuchEmailException;
import rocket.planet.util.security.UserDetailsImpl;

@Service
@RequiredArgsConstructor
public class ProfileVisitorService {

	private final ProfileVisitorRepository profileVisitorRepository;

	public List<ProfileVisitorListResDto> getVisitorList() throws RedisException {
		String loginUserId = Optional
			.ofNullable(UserDetailsImpl.getLoginUserId())
			.orElseThrow(() -> new NoSuchEmailException());
		List<ProfileVisitor> list = (List<ProfileVisitor>)profileVisitorRepository.findAllById(
			Collections.singleton(loginUserId));
		return list.stream()
			.map(e -> ProfileVisitorListResDto.builder().userRole(e.getRole())
				.userId(e.getId()).build()).collect(Collectors.toList());
	}
}
