package rocket.planet.service.profile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
import rocket.planet.dto.profile.ProfileDto;
import rocket.planet.repository.jpa.CertRepository;
import rocket.planet.repository.jpa.PfTechRepository;
import rocket.planet.repository.jpa.PjtRecordRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.PvisitorRepository;
import rocket.planet.repository.jpa.TechRepository;
import rocket.planet.repository.jpa.UserPjtRepository;
import rocket.planet.repository.jpa.UserRepository;
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
				.map(org -> ProfileDto.OrgResDto.builder()
					.deptName(org.getDepartment().getDeptName())
					.teamName(org.getTeam().getTeamName())
					.build())
				.orElse(null);

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
		}).orElse(null);
	}

	public void modifyProfile(ProfileDto.ProfileUpDateResDto profileUpDateResDto) {
		Optional<Profile> updateProfile = profileRepository.findByUserNickName(profileUpDateResDto.getUserNickName());
		updateProfile.ifPresent(profile -> {
			updateProfile.get().updateProfile(profileUpDateResDto);
			profileRepository.save(profile);
		});
	}

	public void modifyProfileDisplay(ProfileDto.ProfileDisplayUpDateResDto displayUpDateResDto) {
		Optional<Profile> updateDisplayStatus = profileRepository.findByUserNickName(
			displayUpDateResDto.getUserNickName());
		updateDisplayStatus.ifPresent(display -> {
			display.updateDisplay(displayUpDateResDto);
			profileRepository.save(display);
		});
	}

	public void modifyAnnualStatus(ProfileDto.AnnualUpDateResDto annualUpDateResDto) {
		Optional<Profile> updateDisplayStatus = profileRepository.findByUserNickName(
			annualUpDateResDto.getUserNickName());
		updateDisplayStatus.ifPresent(annual -> {
			annual.updateAnnual(annualUpDateResDto);
			profileRepository.save(annual);
		});
	}

	@Transactional
	public void addOusideProject(ProfileDto.OutsideProjectRegisterReqDto registerResDto) {
		Optional<Profile> profile = profileRepository.findByUserNickName(registerResDto.getUserNickName());
		PjtRecord pjtRecord = PjtRecord.builder()
			.profile(profile.get())
			.pjtName(registerResDto.getPjtName())
			.pjtTech(registerResDto.getPjtTech())
			.pjtDesc(registerResDto.getPjtDesc())
			.pjtStartDt(registerResDto.getPjtStartDt())
			.pjtEndDt(registerResDto.getPjtEndDt())
			.pjtUserTech(registerResDto.getPjtUserTech())
			.build();
		pjtRecordRepository.save(pjtRecord);

	}

	@Transactional
	public void modifyOusideProject(ProfileDto.OutsideProjectUpdateReqDto updateReqDto) {
		Optional<PjtRecord> updatePjt = pjtRecordRepository.findByPjtName(updateReqDto.getPjtName());
		log.info("pjtRecord : {}", updatePjt);
		updatePjt.get().updatePjtRecord(updateReqDto);

	}

	@Transactional
	public void removeOutsideProject(String pjtUidString) {
		UUID pjtUid = UUID.fromString(pjtUidString);
		pjtRecordRepository.deleteById(pjtUid);
	}

	@Transactional
	public void addCertification(ProfileDto.CertRegisterResDto certRegisterResDto) {
		Optional<Profile> profile = profileRepository.findByUserNickName(certRegisterResDto.getUserNickName());
		Certification cert = Certification.builder()
			.profile(profile.get())
			.certName(certRegisterResDto.getCertName())
			.certAgency(certRegisterResDto.getCertAgency())
			.certType(certRegisterResDto.getCertType())
			.certNumber(certRegisterResDto.getCertNumber())
			.certDt(certRegisterResDto.getCertDt())
			.certExpireDate(certRegisterResDto.getCertExpireDate())
			.build();
		certRepository.save(cert);

	}

	@Transactional
	public void removeCertification(String cetUidString) {
		UUID certUid = UUID.fromString(cetUidString);
		certRepository.deleteById(certUid);
	}

	public boolean checkTech(String techName) {
		return techRepository.findByTechNameIgnoreCase(techName).isPresent();
	}

	@Transactional
	public void addUserTech(ProfileDto.TechRegisterReqDto techReqDto) {
		Optional<Profile> existingProfile = profileRepository.findByUserNickName(techReqDto.getUserNickName());
		Optional<Tech> existingTech = techRepository.findByTechNameIgnoreCase(techReqDto.getTechName());
		if (checkTech(techReqDto.getTechName())) {

			if (existingProfile.isPresent() && existingTech.isPresent()) {
				Profile profile = existingProfile.get();
				Tech tech = existingTech.get();

				// 이미 등록된 기술의 경우 중복 등록 불가능
				boolean isTechAlreadyRegistered = pfTechRepository.existsByProfile_UserNickNameAndTech_TechName(
					profile.getUserName(), tech.getTechName());
				if (isTechAlreadyRegistered) {
					throw new UserTechException("이미 등록된 기술입니다.");
				}

				// 사용자는 최대 다섯 개의 기술만 등록 가능
				List<ProfileTech> userTechList = pfTechRepository.findByProfile_UserNickName(profile.getUserName());
				if (userTechList.size() >= 5) {
					throw new UserTechException("최대 다섯 개의 기술만 등록할 수 있습니다.");
				}
				ProfileTech userTech = ProfileTech.builder()
					.profile(profile)
					.tech(tech)
					.build();
				pfTechRepository.save(userTech);
			}
		} else {
			throw new UserTechException("등록되지 않은 기술로 추가할 수 없습니다.");
		}

	}

	@Transactional
	public void removeUserTech(String userTechIdString) {
		UUID userTechId = UUID.fromString(userTechIdString);
		pfTechRepository.deleteById(userTechId);
	}

	@Transactional
	public void modifyUserInsideProject(ProfileDto.insideProjectUpdateReqDto insidePjtUpdateReqDto) {
		Optional<UserProject> userProject = userPjtRepository.findByProject_ProjectNameAndAndProfile_UserNickName(
			insidePjtUpdateReqDto.getProjectName(), insidePjtUpdateReqDto.getUserNickName());
		userProject.get().updateUserPjtDesc(insidePjtUpdateReqDto);
	}

	@Transactional
	public void changeUserPwd(ProfileDto.UserNewPwdReqDto newPwdReqDto) {
		Optional<User> user = userRepository.findByUserId(newPwdReqDto.getUserId());
		if (passwordEncoder.matches(newPwdReqDto.getUserPwd(), user.get().getUserPwd())) {
			throw new UserPwdCheckException("이전에 사용하던 비밀번호와 동일합니다.");
		}
		if (!newPwdReqDto.getUserPwd().equals(newPwdReqDto.getUserPwdCheck())) {
			throw new UserPwdCheckException("변경하려는 비밀번호가 동일하지 않습니다.");
		}
		user.get().changeUserPwd(newPwdReqDto);
	}

	@Transactional
	public void addProfileVisitor(ProfileDto.VisitorReqDto visitorReqDto) {
		Optional<Profile> pOwner = profileRepository.findByUserNickName(visitorReqDto.getOwnerNickName());
		Optional<Profile> pVisitor = profileRepository.findByUserNickName(visitorReqDto.getVisitorNickName());
		Optional<ProfileVisitor> duplicateV = pvisitorRepository.findByVisitor_UserNickNameAndOwner_UserNickName(
			visitorReqDto.getVisitorNickName(), visitorReqDto.getOwnerNickName());

		if (duplicateV.isPresent()) {
			duplicateV.get().updateVisitTime();
		} else {
			if (!visitorReqDto.getVisitorNickName().equals(visitorReqDto.getOwnerNickName())) {
				ProfileVisitor profileVisitor = ProfileVisitor.builder()
					.visitor(pVisitor.get())
					.owner(pOwner.get())
					.build();
				pvisitorRepository.save(profileVisitor);
			}

		}

	}
}

