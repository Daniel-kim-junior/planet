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
    public List<ProfileReqDto> getProfileDetailByUserNickName(String userNickName) {

        Optional<Profile> profiles = profileRepository.selectProfileByUserNickName(userNickName);
        List<ProfileReqDto> profileDetails = profiles.stream()
                .map(profile -> {
                    List<Org> orgList = profile.getOrg();
                    List<UserProject> projectList = profile.getUserProject();
                    List<ProfileTech> profileTechList = profile.getProfileTech();
                    List<PjtRecord> extPjtRecordList = profile.getExtPjtRecord();
                    List<Certification> certList = profile.getCertification();

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
                            .userId(profile.getUserId())
                            .userNickName(profile.getUserNickName())
                            .org(orgDtoList)
                            .userProject(projectDtoList)
                            .role(profile.getRole().toString())
                            .profileDisplay(profile.isProfileDisplay())
                            .profileCareer(profile.getProfileCareer())
                            .profileBirthDt(profile.getProfileBirthDt())
                            .profileAnnualStatus(profile.isProfileAnnualStatus())
                            .profileStartDate(profile.getProfileStartDate())
                            .profileTech(profileTechDtoList)
                            .extPjtRecord(extPjtRecordDtoList)
                            .certification(certReqDtoList)
                            .build();
                })
                .collect(Collectors.toList());

        log.debug("profileDetails : {}", profileDetails);
        return profileDetails;
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

