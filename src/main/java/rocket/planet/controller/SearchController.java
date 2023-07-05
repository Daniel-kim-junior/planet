package rocket.planet.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rocket.planet.domain.User;
import rocket.planet.dto.Search.SearchUserReqDto;
import rocket.planet.repository.jpa.UserRepository;

import java.util.List;

@RequestMapping("/api/search")
@RestController
@Slf4j
public class SearchController {
    private UserRepository userRepository;

    public SearchController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{searchWord}")
    public ResponseEntity searchAll(@PathVariable String searchWord){
        try {
            List<User> result = userRepository.findAllByUserIdContaining(searchWord);
            log.debug("result : {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new RuntimeException("", e);
        }
    }
}
