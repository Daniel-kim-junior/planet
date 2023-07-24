package rocket.planet.service.profile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.domain.Certification;
import rocket.planet.domain.Org;
import rocket.planet.domain.PjtRecord;
import rocket.planet.domain.Profile;
import rocket.planet.domain.ProfileTech;
import rocket.planet.domain.ProfileVisitor;
import rocket.planet.domain.Tech;
import rocket.planet.domain.User;
import rocket.planet.domain.UserProject;
import rocket.planet.dto.common.CommonResDto;
import rocket.planet.dto.profile.ProfileDto;
import rocket.planet.repository.jpa.CertRepository;
import rocket.planet.repository.jpa.PfTechRepository;
import rocket.planet.repository.jpa.PjtRecordRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.PvisitorRepository;
import rocket.planet.repository.jpa.TechRepository;
import rocket.planet.repository.jpa.UserPjtRepository;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.util.exception.DuplicateException;
import rocket.planet.util.exception.ReqNotFoundException;
import rocket.planet.util.exception.UserPwdCheckException;
import rocket.planet.util.exception.UserTechException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

	private final ProfileRepository profileRepository;
	private final PjtRecordRepository pjtRecordRepository;
	private final CertRepository certRepository;
	private final TechRepository techRepository;
	private final PfTechRepository pfTechRepository;
	private final UserPjtRepository userPjtRepository;
	private final UserRepository userRepository;
	private final PvisitorRepository pvisitorRepository;

	private final PasswordEncoder passwordEncoder;

	@Transactional
	public ProfileDto.ProfileResDto getProfileDetailByUserNickName(String userNickName) {

		Optional<Profile> profile = profileRepository.selectProfileByUserNickName(userNickName);
		List<ProfileVisitor> profileVisitor = pvisitorRepository.findByOwner_UserNickName(userNickName);

		return profile.map(profileD -> {
			List<Org> orgList = profileD.getOrg();
			List<UserProject> projectList = profileD.getUserProject();
			List<ProfileTech> profileTechList = profileD.getProfileTech();
			List<PjtRecord> extPjtRecordList = profileD.getExtPjtRecord();
			List<Certification> certList = profileD.getCertification();

			ProfileDto.OrgResDto orgDto = orgList.stream()
				.findFirst()
				.map(org -> {
					return ProfileDto.OrgResDto.builder()
						.deptName(org.getDepartment().getDeptName())
						.teamName(org.getTeam().getTeamName())
						.build();
				})
				.orElseGet(() -> ProfileDto.OrgResDto.builder()
					.deptName("무소속")
					.teamName("무소속")
					.build());

			List<ProfileDto.UserInProgressProjectResDto> inProgressProjectList = projectList.stream()
				.filter(project -> project.getUserPjtCloseDt().equals(LocalDate.of(2999, 12, 31)))
				.map(project -> ProfileDto.UserInProgressProjectResDto.builder()
					.projectName(project.getProject().getProjectName())
					.userPjtCloseDt(project.getUserPjtCloseDt())
					.build())
				.collect(Collectors.toList());

			List<ProfileDto.ClosedInsideProjectResDto> closedProjectDtoList = projectList.stream()
				.filter(project -> !project.getUserPjtCloseDt().equals(LocalDate.of(2999, 12, 31)))
				.map(project -> ProfileDto.ClosedInsideProjectResDto.builder()
					.projectName(project.getProject().getProjectName())
					.projectDesc(project.getProject().getProjectDesc())
					.teamName(project.getProject().getTeam().getTeamName())
					.userPjtJoinDt(project.getUserPjtJoinDt())
					.userPjtCloseDt(project.getUserPjtCloseDt())
					.userPjtDesc(project.getUserPjtDesc())
					.build())
				.collect(Collectors.toList());

			List<ProfileDto.ProfileTechResDto> profileTechDtoList = profileTechList.stream()
				.map(profileTech -> ProfileDto.ProfileTechResDto.builder()
					.userTechId(profileTech.getId())
					.techName(profileTech.getTech().getTechName())
					.build())
				.collect(Collectors.toList());

			List<ProfileDto.OutsideProjectResDto> extPjtRecordDtoList = extPjtRecordList.stream()
				.map(extPjtRecord -> ProfileDto.OutsideProjectResDto.builder()
					.pjtUid(extPjtRecord.getId())
					.pjtName(extPjtRecord.getPjtName())
					.pjtDesc(extPjtRecord.getPjtDesc())
					.pjtStartDt(extPjtRecord.getPjtStartDt())
					.pjtEndDt(extPjtRecord.getPjtEndDt())
					.pjtTech(extPjtRecord.getPjtTech())
					.pjtUserTech(extPjtRecord.getPjtUserTech())
					.build())
				.collect(Collectors.toList());

			List<ProfileDto.CertResDto> certReqDtoList = certList.stream()
				.map(cert -> ProfileDto.CertResDto.builder()
					.certUid(cert.getId())
					.certName(cert.getCertName())
					.certType(cert.getCertType())
					.certDt(cert.getCertDt())
					.certAgency(cert.getCertAgency())
					.certExpireDate(cert.getCertExpireDate())
					.certNumber(cert.getCertNumber())
					.build())
				.collect(Collectors.toList());

			List<ProfileDto.VisitorResDto> visitors = profileVisitor.stream()
				.map(pVisitor -> ProfileDto.VisitorResDto.builder()
					.visitorNickName(pVisitor.getVisitor().getUserNickName())
					.visitorRole(pVisitor.getVisitor().getRole().toString())
					.build())
				.collect(Collectors.toList());

			return ProfileDto.ProfileResDto.builder()
				.userId(profileD.getUserId())
				.userNickName(profileD.getUserNickName())
				.org(orgDto)
				.userInProgressProject(inProgressProjectList)
				.userClosedProject(closedProjectDtoList)
				.visitor(visitors)
				.role(profileD.getRole().toString())
				.profileDisplay(profileD.isProfileDisplay())
				.profileCareer(profileD.getProfileCareer())
				.profileBirthDt(profileD.getProfileBirthDt())
				.profileAnnualStatus(profileD.isProfileAnnualStatus())
				.profileStartDate(profileD.getProfileStartDate())
				.profileTech(profileTechDtoList)
				.extPjtRecord(extPjtRecordDtoList)
				.certification(certReqDtoList)
				.build();
		}).orElseThrow(() -> new UserTechException("님의 프로필은 존재하지 않습니다."));
	}

	public CommonResDto modifyProfile(ProfileDto.ProfileUpDateResDto profileUpDateResDto, String loginUser) {
		Profile updateProfile = profileRepository.findByUserNickName(profileUpDateResDto.getUserNickName())
			.orElseThrow(() -> new ReqNotFoundException("님의 프로필은 존재하지 않습니다."));
		if (!updateProfile.getUserNickName().equals(loginUser)) {
			throw new AccessDeniedException(
				loginUser + "님은 " + profileUpDateResDto.getUserNickName() + "님의 프로필을 수정할 권한이 없습니다.");
		}
		updateProfile.updateProfile(profileUpDateResDto);
		profileRepository.save(updateProfile);
		return CommonResDto.builder().message(profileUpDateResDto.getUserNickName() + "님이 프로필 수정하였습니다.").build();
	}

	public CommonResDto modifyProfileDisplay(ProfileDto.ProfileDisplayUpDateResDto displayUpDateResDto,
		String loginUser) {
		Profile updateDisplayStatus = profileRepository.findByUserNickName(
			displayUpDateResDto.getUserNickName()).orElseThrow(() -> new ReqNotFoundException("님의 프로필은 존재하지 않습니다."));
		if (!updateDisplayStatus.getUserNickName().equals(loginUser)) {
			throw new AccessDeniedException(
				loginUser + "님은 " + displayUpDateResDto.getUserNickName() + "님의 프로필을 공개여부를 수정할 권한이 없습니다.");
		}
		updateDisplayStatus.updateDisplay(displayUpDateResDto);
		profileRepository.save(updateDisplayStatus);
		if (displayUpDateResDto.isProfileDisplay()) {
			return CommonResDto.builder()
				.message(displayUpDateResDto.getUserNickName() + "님의 프로필이 공개로 변경되었습니다.")
				.build();
		} else {
			return CommonResDto.builder()
				.message(displayUpDateResDto.getUserNickName() + "님의 프로필이 비공개로 변경되었습니다.")
				.build();
		}
	}

	public CommonResDto modifyAnnualStatus(ProfileDto.AnnualUpDateResDto annualUpDateResDto, String loginUser) {
		Profile updateDisplayStatus = profileRepository.findByUserNickName(
			annualUpDateResDto.getUserNickName()).orElseThrow(() -> new ReqNotFoundException("님의 프로필은 존재하지 않습니다."));
		if (!updateDisplayStatus.getUserNickName().equals(loginUser)) {
			throw new AccessDeniedException(
				loginUser + "님은 " + annualUpDateResDto.getUserNickName() + "님의 휴가여부를 수정할 권한이 없습니다.");
		}
		updateDisplayStatus.updateAnnual(annualUpDateResDto);
		profileRepository.save(updateDisplayStatus);
		if (annualUpDateResDto.isProfileAnnualStatus()) {
			return CommonResDto.builder()
				.message(annualUpDateResDto.getUserNickName() + "님의 상태가 휴가중으로 변경되었습니다.")
				.build();
		} else {
			return CommonResDto.builder()
				.message(annualUpDateResDto.getUserNickName() + "님의 상태가 출근으로 변경되었습니다.")
				.build();
		}
	}

	@Transactional
	public CommonResDto addOusideProject(ProfileDto.OutsideProjectRegisterReqDto registerReqDto, String loginUser) {
		Profile profile = profileRepository.findByUserNickName(registerReqDto.getUserNickName())
			.orElseThrow(() -> new ReqNotFoundException("님의 프로필은 존재하지 않습니다."));
		if (!profile.getUserNickName().equals(loginUser)) {
			throw new AccessDeniedException(
				loginUser + "님은 " + registerReqDto.getUserNickName() + "님의 사외프로젝트를 추가할 권한이 없습니다.");
		}
		Optional<PjtRecord> pjtR = pjtRecordRepository.findByPjtName(registerReqDto.getPjtName());
		if (pjtR.isPresent()) {
			throw new DuplicateException("중복되는 이름을 가진 사외프로젝트가 존재합니다. 이미 프로젝트명을 수정해주세요.");
		}

		PjtRecord pjtRecord = PjtRecord.builder()
			.profile(profile)
			.pjtName(registerReqDto.getPjtName())
			.pjtTech(registerReqDto.getPjtTech())
			.pjtDesc(registerReqDto.getPjtDesc())
			.pjtStartDt(registerReqDto.getPjtStartDt())
			.pjtEndDt(registerReqDto.getPjtEndDt())
			.pjtUserTech(registerReqDto.getPjtUserTech())
			.build();
		pjtRecordRepository.save(pjtRecord);
		return CommonResDto.builder().message("외부프로젝트 생성이 완료되었습니다.").build();
	}

	@Transactional
	public CommonResDto modifyOusideProject(ProfileDto.OutsideProjectUpdateReqDto updateReqDto, String loginUser) {
		PjtRecord updatePjt = pjtRecordRepository.findByPjtName(updateReqDto.getPjtName())
			.orElseThrow(() -> new ReqNotFoundException("수정할 외부프로젝트가 존재하지 않습니다."));
		if (!updatePjt.getProfile().getUserNickName().equals(loginUser)) {
			throw new AccessDeniedException(
				loginUser + "님은 " + updateReqDto.getUserNickName() + "님의 사외 프로젝트를 수정할 권한이 없습니다.");
		}
		updatePjt.updatePjtRecord(updateReqDto);
		return CommonResDto.builder().message(updatePjt.getPjtName() + "프로젝트를 수정하였습니다.").build();
	}

	@Transactional
	public CommonResDto removeOutsideProject(String pjtUidString, String loginUser) {
		UUID pjtUid = UUID.fromString(pjtUidString);
		PjtRecord pjt = pjtRecordRepository.findById(pjtUid)
			.orElseThrow(() -> new ReqNotFoundException("삭제할 외부프로젝트가 존재하지 않습니다."));
		if (!pjt.getProfile().getUserNickName().equals(loginUser)) {
			throw new AccessDeniedException(
				loginUser + "님은 " + pjt.getProfile().getUserNickName() + "님의 사외 프로젝트인 " + pjt.getPjtName()
					+ " 을 삭제할 권한이 없습니다.");
		}
		pjtRecordRepository.deleteById(pjtUid);
		return CommonResDto.builder().message(pjt.getPjtName() + " 을 삭제했습니다.").build();
	}

	@Transactional
	public CommonResDto addCertification(ProfileDto.CertRegisterResDto certRegisterResDto, String loginUser) {
		Profile profile = profileRepository.findByUserNickName(certRegisterResDto.getUserNickName())
			.orElseThrow(() -> new ReqNotFoundException("님의 프로필은 존재하지 않습니다."));
		if (!profile.getUserNickName().equals(loginUser)) {
			throw new AccessDeniedException(loginUser + "님은 " + profile.getUserName() + "님의 프로필에 자격증을 등록할 수 없습니다.");
		}
		Certification cert = Certification.builder()
			.profile(profile)
			.certName(certRegisterResDto.getCertName())
			.certAgency(certRegisterResDto.getCertAgency())
			.certType(certRegisterResDto.getCertType())
			.certNumber(certRegisterResDto.getCertNumber())
			.certDt(certRegisterResDto.getCertDt())
			.certExpireDate(certRegisterResDto.getCertExpireDate())
			.build();
		certRepository.save(cert);
		return CommonResDto.builder().message("자격증 생성이 완료되었습니다.").build();
	}

	@Transactional
	public CommonResDto removeCertification(String cetUidString, String loginUser) {
		UUID certUid = UUID.fromString(cetUidString);
		Certification cert = certRepository.findById(certUid)
			.orElseThrow(() -> new ReqNotFoundException("삭제할 자격증이 존재하지 않습니다."));
		if (!cert.getProfile().getUserNickName().equals(loginUser)) {
			throw new AccessDeniedException(
				loginUser + "님은 " + cert.getProfile().getUserNickName() + "님의 " + cert.getCertName()
					+ " 자격증을 삭제할 권한이 없습니다.");
		}
		certRepository.deleteById(certUid);
		return CommonResDto.builder().message(cert.getCertName() + " 자격증을 삭제했습니다.").build();
	}

	@Transactional
	public CommonResDto addUserTech(ProfileDto.TechRegisterReqDto techReqDto, String loginUser) {
		Profile profile = profileRepository.findByUserNickName(techReqDto.getUserNickName())
			.orElseThrow(() -> new ReqNotFoundException(techReqDto.getUserNickName() + "님의 프로필은 존재하지 않습니다."));

		if (!profile.getUserNickName().equals(loginUser)) {
			throw new AccessDeniedException(
				loginUser + "님은 " + profile.getUserNickName() + "님의 프로필에 메인 기술을 등록할 수 없습니다.");
		}

		Tech tech = techRepository.findByTechNameIgnoreCase(techReqDto.getTechName())
			.orElseThrow(() -> new UserTechException("등록되지 않은 기술로 추가할 수 없습니다."));

		if (pfTechRepository.existsByProfile_UserNickNameAndTech_TechName(profile.getUserNickName(),
			tech.getTechName())) {
			throw new UserTechException("이미 등록된 기술입니다.");
		}

		List<ProfileTech> userTechList = pfTechRepository.findByProfile_UserNickName(profile.getUserNickName());
		if (userTechList.size() >= 5) {
			throw new UserTechException("최대 다섯 개의 기술만 등록할 수 있습니다.");
		}
		ProfileTech userTech = ProfileTech.builder()
			.profile(profile)
			.tech(tech)
			.build();
		pfTechRepository.save(userTech);
		return CommonResDto.builder()
			.message(techReqDto.getUserNickName() + "님의 프로필에 " + techReqDto.getTechName() + " 기술을 등록하였습니다.")
			.build();
	}

	@Transactional
	public CommonResDto removeUserTech(String userTechIdString, String loginUser) {
		UUID userTechId = UUID.fromString(userTechIdString);
		ProfileTech tech = pfTechRepository.findById(userTechId)
			.orElseThrow(() -> new ReqNotFoundException("삭제할 기술이 존재하지 않습니다."));
		if (!tech.getProfile().getUserNickName().equals(loginUser)) {
			throw new AccessDeniedException(
				loginUser + "님은 " + tech.getProfile().getUserNickName() + "님의 메인기술인 " + tech.getTech().getTechName()
					+ "을 삭제할 권한이 없습니다.");
		}
		pfTechRepository.deleteById(userTechId);
		return CommonResDto.builder().message(tech.getTech().getTechName() + " 기술을 메인 기술에서 삭제하였습니다.").build();
	}

	@Transactional
	public CommonResDto modifyUserInsideProject(ProfileDto.insideProjectUpdateReqDto insidePjtUpdateReqDto,
		String loginUser) {
		UserProject userProject = userPjtRepository.findByProject_ProjectNameAndAndProfile_UserNickName(
				insidePjtUpdateReqDto.getProjectName(), insidePjtUpdateReqDto.getUserNickName())
			.orElseThrow(() -> new ReqNotFoundException("수정 가능한 사내프로젝트가 존재하지 않습니다."));
		;
		if (!userProject.getProfile().getUserNickName().equals(loginUser)) {
			throw new AccessDeniedException(
				loginUser + "님은 " + userProject.getProfile().getUserNickName() + "님의 " + userProject.getProject()
					.getProjectName() + " 프로젝트 상세 설명을 수정할 권한이 없습니다.");
		}
		userProject.updateUserPjtDesc(insidePjtUpdateReqDto);
		return CommonResDto.builder()
			.message(insidePjtUpdateReqDto.getProjectName() + "님의 " + insidePjtUpdateReqDto.getProjectName()
				+ " 프로젝트를 상세이력을 수정하였습니다.")
			.build();
	}

	@Transactional
	public CommonResDto changeUserPwd(ProfileDto.UserNewPwdReqDto newPwdReqDto, String loginUser) {
		User user = userRepository.findByUserId(newPwdReqDto.getUserId())
			.orElseThrow(() -> new UserPwdCheckException("유저를 찾을 수 없습니다."));

		if (!user.getProfile().getUserId().equals(loginUser)) {
			throw new AccessDeniedException(
				loginUser + "계정은 " + user.getProfile().getUserNickName() + "님의 비밀번호를 변경할 수 없습니다.");
		}

		if (newPwdReqDto.getUserPwdCheck() == null) {
			if (passwordEncoder.matches(newPwdReqDto.getUserPwd(), user.getUserPwd())) {
				throw new UserPwdCheckException("이전에 사용하던 비밀번호와 동일합니다.");
			} else {
				return CommonResDto.builder().message("해당 비밀번호로 변경 가능합니다.").build();
			}
		} else {
			if (!passwordEncoder.matches(newPwdReqDto.getUserPwd(), user.getUserPwd())) {
				if (newPwdReqDto.getUserPwd().equals(newPwdReqDto.getUserPwdCheck())) {
					user.changeUserPwd(newPwdReqDto);
					return CommonResDto.builder().message("비밀번호가 성공적으로 변경되었습니다.").build();
				} else {
					throw new UserPwdCheckException("변경하려는 두 비밀번호가 서로 일치하지 않습니다.");
				}
			} else {
				throw new UserPwdCheckException("이전에 사용하던 비밀번호와 동일합니다.");
			}
		}

	}

	@Transactional
	public CommonResDto addProfileVisitor(ProfileDto.VisitorReqDto visitorReqDto) {
		Profile pOwner = profileRepository.findByUserNickName(visitorReqDto.getOwnerNickName())
			.orElseThrow(() -> new ReqNotFoundException("로그인 후 방문해주세요."));

		Profile pVisitor = profileRepository.findByUserNickName(visitorReqDto.getVisitorNickName())
			.orElseThrow(() -> new ReqNotFoundException("해당 프로필은 존재하지 않습니다."));
		Optional<ProfileVisitor> duplicateV = pvisitorRepository.findByVisitor_UserNickNameAndOwner_UserNickName(
			visitorReqDto.getVisitorNickName(), visitorReqDto.getOwnerNickName());
		if (duplicateV.isPresent()) {
			duplicateV.get().updateVisitTime();
		} else {
			if (!visitorReqDto.getVisitorNickName().equals(visitorReqDto.getOwnerNickName())) {
				ProfileVisitor profileVisitor = ProfileVisitor.builder()
					.visitor(pVisitor)
					.owner(pOwner)
					.build();
				pvisitorRepository.save(profileVisitor);
			}
		}
		return CommonResDto.builder()
			.message(visitorReqDto.getVisitorNickName() + "님이 " + visitorReqDto.getOwnerNickName() + "님의 프로필을 방문하였습니다.")
			.build();
	}
}

