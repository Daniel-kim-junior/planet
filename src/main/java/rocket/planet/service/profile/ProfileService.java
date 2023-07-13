package rocket.planet.service.profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocket.planet.domain.*;
import rocket.planet.dto.profile.*;
import rocket.planet.repository.jpa.CertRepository;
import rocket.planet.repository.jpa.PjtRecordRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.util.security.UserDetailsImpl;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final PjtRecordRepository pjtRecordRepository;
    private final CertRepository certRepository;

    @Transactional
    public ProfileDto.ProfileResDto getProfileDetailByUserNickName(String userNickName) {

        Optional<Profile> profile = profileRepository.selectProfileByUserNickName(userNickName);
        return profile.map(profileD -> {
            List<Org> orgList =  profileD.getOrg();
            List<UserProject> projectList = profileD.getUserProject();
            List<ProfileTech> profileTechList = profileD.getProfileTech();
            List<PjtRecord> extPjtRecordList = profileD.getExtPjtRecord();
            List<Certification> certList = profileD.getCertification();

            ProfileDto.OrgResDto orgDto = orgList.stream()
                    .findFirst()
                    .map(org -> ProfileDto.OrgResDto.builder()
                            .orgStatus(org.isOrgStatus())
                            .deptName(org.getDepartment().getDeptName())
                            .teamName(org.getTeam().getTeamName())
                            .build())
                    .orElse(null);


            List<ProfileDto.InsideProjectResDto> projectDtoList = projectList.stream()
                    .map(project -> ProfileDto.InsideProjectResDto.builder()
                            .projectName(project.getProject().getProjectName())
                            .projectDesc(project.getProject().getProjectDesc())
                            .projectStartDt(project.getProject().getProjectStartDt())
                            .projectEndDt(project.getProject().getProjectEndDt())
                            .build())
                    .collect(Collectors.toList());

            List<ProfileDto.ProfileTechResDto> profileTechDtoList = profileTechList.stream()
                    .map(profileTech -> ProfileDto.ProfileTechResDto.builder()
                            .techName(profileTech.getTech().getTechName())
                            .build())
                    .collect(Collectors.toList());

            List<ProfileDto.OutsideProjectResDto> extPjtRecordDtoList = extPjtRecordList.stream()
                    .map(extPjtRecord -> ProfileDto.OutsideProjectResDto.builder()
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
                            .certName(cert.getCertName())
                            .certType(cert.getCertType())
                            .certDt(cert.getCertDt())
                            .certAgency(cert.getCertAgency())
                            .certExpireDate(cert.getCertExpireDate())
                            .certNumber(cert.getCertNumber())
                            .build())
                    .collect(Collectors.toList());

            return ProfileDto.ProfileResDto.builder()
                    .userId(profileD.getUserId())
                    .userNickName(profileD.getUserNickName())
                    .org(orgDto)
                    .userProject(projectDtoList)
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
        Optional<Profile> updateDisplayStatus = profileRepository.findByUserNickName(displayUpDateResDto.getUserNickName());
        updateDisplayStatus.ifPresent(display -> {
            display.updateDisplay(displayUpDateResDto);
            profileRepository.save(display);
        });
    }
    public void modifyAnnualStatus(ProfileDto.AnnualUpDateResDto annualUpDateResDto) {
        Optional<Profile> updateDisplayStatus = profileRepository.findByUserNickName(annualUpDateResDto.getUserNickName());
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
    public void removeOutsideProject(ProfileDto.OutsideProjectDeleteReqDto outPjtDeleteReqDto) {
        pjtRecordRepository.deletePjtRecordByPjtName(outPjtDeleteReqDto.getPjtName());
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
    public void modifyCertification(ProfileDto.CertUpdateResDto certUpdateResDto) {
            Optional<Certification> updateCert = certRepository.findByCertNumber(certUpdateResDto.getCertNumber());
            updateCert.get().updateCert(certUpdateResDto);
    }
    @Transactional
    public void removeCertification(ProfileDto.CertDeleteReqDto certDeleteReqDto) {
            certRepository.deleteCertificationByCertNumber(certDeleteReqDto.getCertNumber());
    }

}

