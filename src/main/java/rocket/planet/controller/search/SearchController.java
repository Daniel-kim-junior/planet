package rocket.planet.controller.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rocket.planet.dto.search.SearchDto;
import rocket.planet.service.search.SearchService;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final SearchService searchService;
    @GetMapping("/{keyword}")
    public ResponseEntity<List<SearchDto.SearchResDto>> searchResultList(@PathVariable("keyword")String keyword) {
        List<SearchDto.SearchResDto> searchList = searchService.getSearchList(keyword);
        return ResponseEntity.ok().body(searchList);
    }
}
