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
                        return SearchDto.SearchOrgResDto.builder()
                                .deptName(team.getDepartment().getDeptName())
                                .teamName(team.getTeam().getTeamName())
                                .userTeamType(String.valueOf(team.getTeam().getTeamType()))
                                .build();
                    })
                    .orElseGet(() -> SearchDto.SearchOrgResDto.builder()
                            .deptName("무소속")
                            .teamName("무소속")
                            .userTeamType("무소속")
                            .build());

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
