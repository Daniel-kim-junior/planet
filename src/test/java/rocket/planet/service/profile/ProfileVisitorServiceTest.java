// package rocket.planet.service.profile;
//
// import java.util.List;
//
// import org.assertj.core.api.Assertions;
// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
//
// import rocket.planet.dto.visitor.ProfileVisitorListResDto;
//
// @SpringBootTest
// class ProfileVisitorServiceTest {
//
// 	@MockBean
// 	private ProfileVisitorService profileVisitorService;
//
// 	@Test
// 	void testGetVisitorList() {
// 		List<ProfileVisitorListResDto> visitorList = profileVisitorService.getVisitorList();
// 		Assertions.assertThat(visitorList).isNotNull();
// 		Assertions.assertThat(visitorList.size()).isEqualTo(0);
// 	}
//
// }