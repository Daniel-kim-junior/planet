package rocket.planet.controller.search;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import rocket.planet.domain.Profile;
import rocket.planet.domain.Tech;
import rocket.planet.dto.profile.ProfileDto;
import rocket.planet.dto.project.ProjectSummaryResDto;
import rocket.planet.dto.search.SearchDto;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.TechRepository;
import rocket.planet.service.profile.ProfileService;
import rocket.planet.service.search.SearchService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class SearchUserControllerTest {

    @Autowired
    private SearchService searchService;



    @DisplayName("검색 테스트")
    @Test
    void 검색_조회_테스트() {
        List<SearchDto.SearchResDto> searchList = searchService.getSearchList("spring boot");

        for (SearchDto.SearchResDto result : searchList)
            System.out.println("================== searchList =========\n" + result);

    }
}