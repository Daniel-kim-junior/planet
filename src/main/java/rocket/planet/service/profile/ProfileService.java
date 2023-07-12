package rocket.planet.service.profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocket.planet.domain.*;
import rocket.planet.dto.profile.*;
import rocket.planet.repository.jpa.PjtRecordRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.dto.profile.ProfileDto.OutsideProjectReqDto;
import rocket.planet.dto.profile.ProfileDto.CertReqDto;
import rocket.planet.dto.profile.ProfileDto.ProfileReqDto;
import rocket.planet.dto.profile.ProfileDto.InsideProjectReqDto;
import rocket.planet.dto.profile.ProfileDto.OrgReqDto;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final PjtRecordRepository pjtRecordRepository;

    @Transactional
    public ProfileDto.ProfileReqDto getProfileDetailByUserNickName(String userNickName) {

        Optional<Profile> profile = profileRepository.selectProfileByUserNickName(userNickName);
        return profile.map(profileD -> {
            List<Org> orgList = profileD.getOrg();
            List<UserProject> projectList = profileD.getUserProject();
            List<ProfileTech> profileTechList = profileD.getProfileTech();
            List<PjtRecord> extPjtRecordList = profileD.getExtPjtRecord();
            List<Certification> certList = profileD.getCertification();

            List<ProfileDto.OrgReqDto> orgDtoList = orgList.stream()
                    .map(org -> OrgReqDto.builder()
                            .orgStatus(org.isOrgStatus())
                            .deptName(org.getDepartment().getDeptName())
                            .teamName(org.getTeam().getTeamName())
                            .build())
                    .collect(Collectors.toList());

            List<ProfileDto.InsideProjectReqDto> projectDtoList = projectList.stream()
                    .map(project -> InsideProjectReqDto.builder()
                            .projectName(project.getProject().getProjectName())
                            .projectDesc(project.getProject().getProjectDesc())
                            .projectStartDt(project.getProject().getProjectStartDt())
                            .projectEndDt(project.getProject().getProjectEndDt())
                            .build())
                    .collect(Collectors.toList());

            List<ProfileDto.ProfileTechReqDto> profileTechDtoList = profileTechList.stream()
                    .map(profileTech -> ProfileDto.ProfileTechReqDto.builder()
                            .techName(profileTech.getTech().getTechName())
                            .build())
                    .collect(Collectors.toList());

            List<ProfileDto.OutsideProjectReqDto> extPjtRecordDtoList = extPjtRecordList.stream()
                    .map(extPjtRecord -> OutsideProjectReqDto.builder()
                            .pjtName(extPjtRecord.getPjtName())
                            .pjtDesc(extPjtRecord.getPjtDesc())
                            .pjtStartDt(extPjtRecord.getPjtStartDt())
                            .pjtEndDt(extPjtRecord.getPjtEndDt())
                            .pjtTech(extPjtRecord.getPjtTech())
                            .pjtUserTech(extPjtRecord.getPjtUserTech())
                            .build())
                    .collect(Collectors.toList());

            List<ProfileDto.CertReqDto> certReqDtoList = certList.stream()
                    .map(cert -> CertReqDto.builder()
                            .certName(cert.getCertName())
                            .certType(cert.getCertType())
                            .certDt(cert.getCertDt())
                            .certAgency(cert.getCertAgency())
                            .certExpireDate(cert.getCertExpireDate())
                            .certNumber(cert.getCertNumber())
                            .build())
                    .collect(Collectors.toList());

            return ProfileDto.ProfileReqDto.builder()
                    .userId(profileD.getUserId())
                    .userNickName(profileD.getUserNickName())
                    .org(orgDtoList)
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


    @Transactional
    public void addOusideProject(ProfileDto.OutsideProjectRegisterResDto registerResDto) {
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
    public void modifyOusideProject(ProfileDto.OutsideProjectUpdateResDto updateResDto) {
        Optional<PjtRecord> updatePjt = pjtRecordRepository.findByPjtName(updateResDto.getPjtName());
        log.info("pjtRecord : {}", updatePjt);
        updatePjt.get().updatePjtRecord(updateResDto);

    }
    @Transactional
    public void removeOutsideProject(String pjtName) {
        pjtRecordRepository.deletePjtRecordByPjtName(pjtName);
    }

}

