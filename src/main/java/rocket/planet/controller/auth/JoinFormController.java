package rocket.planet.controller.auth;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.TeamRepository;
import rocket.planet.util.aop.ExceptionAdvice;
import rocket.planet.util.exception.ExceptionEnum;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class JoinFormController {

	private final DeptRepository deptRepository;

	private final TeamRepository teamRepository;

	@GetMapping("/join-dept")
	public ResponseEntity<List<String>> joinFormDeptList() throws Exception {
		return ResponseEntity.ok().body(deptRepository.findDeptNameAll());
	}

	@GetMapping("/join-team")
	public ResponseEntity<List<String>> joinFormTeamList(
		@Valid @NotBlank @NotEmpty @RequestParam String deptName) throws
		Exception {
		return ResponseEntity.ok().body(teamRepository.findTeamNameByDeptName(deptName).stream()
			.map(e -> e.getTeamName()).collect(Collectors.toList()));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity exception(Exception e) {
		return ResponseEntity.badRequest()
			.body(ExceptionAdvice.getCommonErrorDto(ExceptionEnum.REQUEST_NOT_VALID_EXCEPTION));
	}

}
