package rocket.planet.service.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocket.planet.domain.*;
import rocket.planet.dto.profile.ProfileDto;
import rocket.planet.dto.search.SearchDto;
import rocket.planet.repository.jpa.ProfileRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {
    private final ProfileRepository profileRepository;

    @Transactional
    public List<SearchDto.SearchResDto> getSearchList(String keyword) {
        List<Profile> searchResult = profileRepository.selectProfilesBySearchKeyword(keyword);
        return searchResult.stream().map(result -> {
           List<ProfileTech> userTechs = result.getProfileTech();
           List<Org> org = result.getOrg();
           List<UserProject> pjt = result.getUserProject();

            List<SearchDto.SearchTechResDto> techList = userTechs.stream()
                    .map(usrTech -> SearchDto.SearchTechResDto.builder()
                            .techName(usrTech.getTech().getTechName())
                            .build())
                    .collect(Collectors.toList());

            SearchDto.SearchOrgResDto teams = org.stream()
                    .findFirst()
                    .map(team -> {
                        Department department = team.getDepartment();
                        String deptName = (department != null && department.getDeptName() != null)
                                ? department.getDeptName()
                                : "무소속";

                        Team orgTeam = team.getTeam();
                        String teamName = (orgTeam != null && orgTeam.getTeamName() != null)
                                ? orgTeam.getTeamName()
                                : "무소속";

                        String userTeamType = (orgTeam != null && orgTeam.getTeamType() != null)
                                ? orgTeam.getTeamType().toString()
                                : "무소속";

                        return SearchDto.SearchOrgResDto.builder()
                                .deptName(deptName)
                                .teamName(teamName)
                                .userTeamType(userTeamType)
                                .build();
                    })
                    .orElse(null);

            List<SearchDto.SearchUserPjtStatusResDto> userPjts = pjt.stream()
                    .filter(project -> project.getUserPjtCloseDt().equals(LocalDate.of(2999, 12, 31)))
                    .map(project -> SearchDto.SearchUserPjtStatusResDto.builder()
                            .projectName(project.getProject().getProjectName())
                            .userPjtCloseDt(project.getUserPjtCloseDt())
                            .build())
                    .collect(Collectors.toList());

            return SearchDto.SearchResDto.builder()
                    .userNickName(result.getUserNickName())
                    .userOrg(teams)
                    .userTech(techList)
                    .userStatus(userPjts)
                    .build();
        }).collect(Collectors.toList());

    }



}
