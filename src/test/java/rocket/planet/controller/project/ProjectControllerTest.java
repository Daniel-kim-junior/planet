package rocket.planet.controller.project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static rocket.planet.dto.project.ProjectDto.*;
import static rocket.planet.dto.project.ProjectUpdateDto.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import rocket.planet.domain.ProjectStatus;
import rocket.planet.dto.common.CommonResDto;
import rocket.planet.dto.project.ProjectCloseResDto;
import rocket.planet.dto.project.ProjectDetailResDto;
import rocket.planet.dto.project.ProjectNameReqDto;
import rocket.planet.dto.project.ProjectRegisterReqDto;
import rocket.planet.dto.project.ProjectSummaryResDto;
import rocket.planet.service.project.ProjectService;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

	@InjectMocks
	private ProjectController projectController;

	@Mock
	private ProjectService projectService;

	@DisplayName("프로젝트 상세 조회 테스트")
	@Test
	void 프로젝트_상세_조회_테스트() {
		NameReqDto request = NameReqDto.builder().name("스마트 건설 TF").build();

		List<String> newMemberList = new ArrayList<>();
		newMemberList.add("plpl");
		newMemberList.add("pilot");
		newMemberList.add("captain");
		newMemberList.add("crew");

		ProjectDetailResDto response = ProjectDetailResDto.builder()
			.projectLastModifiedBy("admin")
			.projectLeader("plpl")
			.projectStatus(String.valueOf(ProjectStatus.ONGOING))
			.projectName("스마트 건설 TF")
			.dept("스마트솔루션")
			.team("스마트시티")
			.lastModifiedDate(LocalDate.now())
			.projectDesc("건설 현장에서 카카오톡을 이용하여 관리를 용이하게 합니다.")
			.projectTech("iOS, Android, Kotlin, MySQL, MongoDB")
			.projectMember(newMemberList)
			.projectStartDt(LocalDate.of(2023, 7, 8))
			.projectEndDt(LocalDate.of(2023, 9, 12))
			.build();

		when(projectService.getProject(any())).thenReturn(response);

		ResponseEntity<ProjectDetailResDto> result = projectController.projectDetails(request);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(response, result.getBody());
	}

	@DisplayName("프로젝트 생성 테스트")
	@Test
	void 프로젝트_생성_테스트() {
		// given
		List<String> memberList = new ArrayList<>();
		memberList.add("plpl");
		memberList.add("pilot");
		memberList.add("captain");

		ProjectRegisterReqDto request = ProjectRegisterReqDto.builder()
			.userNickName("captain")
			.projectName("스마트 시티 TF")
			.projectDesc("스마트 시티에 대한 단기 목표를 달성하기 위해 만들었습니다.")
			.projectTech("Spring, Java, Computer Vision")
			.projectLeader("plpl")
			.deptName("스마트솔루션")
			.teamName("스마트시티")
			.projectMember(memberList)
			.projectStartDt(LocalDate.of(2023, 7, 5))
			.projectEndDt(LocalDate.of(2023, 8, 14))
			.projectStartDt(LocalDate.of(2023, 7, 5))
			.projectEndDt(LocalDate.of(2023, 8, 14))
			.build();

		CommonResDto response = CommonResDto.builder().message("프로젝트 생성이 완료되었습니다.").build();

		when(projectService.saveProject(any())).thenReturn(response);

		ResponseEntity<CommonResDto> result = projectController.projectSave(request);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(response, result.getBody());

	}

	@Test
	void 프로젝트_수정_테스트() {
		List<String> newMemberList = new ArrayList<>();
		newMemberList.add("plpl");
		newMemberList.add("pilot");
		newMemberList.add("captain");
		newMemberList.add("crew");

		ProjectUpdateDetailDto request = ProjectUpdateDetailDto.builder()
			.userNickName("captain")
			.projectName("스마트 건설 TF")
			.projectDesc("(수정) 건설 현장에서 카카오톡을 이용하여 관리를 용이하게 합니다.")
			.projectTech("iOS, Android, Kotlin, MySQL, MongoDB")
			.projectStartDt(LocalDate.of(2023, 7, 8))
			.projectEndDt(LocalDate.of(2023, 9, 12))
			.projectLeader("plpl")
			.projectMember(newMemberList)
			.build();

		CommonResDto response = CommonResDto.builder().message("해당 프로젝트 수정이 완료되었습니다.").build();

		when(projectService.updateProjectDetail(any())).thenReturn(response);

		ResponseEntity<CommonResDto> result = projectController.projectDetailModify(request);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(response, result.getBody());

	}

	@Test
	void 프로젝트_삭제_테스트() {
		ProjectUpdateStatusDto request = ProjectUpdateStatusDto.builder()
			.authType("PILOT")
			.projectName("카카오톡 IT 솔루션")
			.userNickName("pilot")
			.build();

		CommonResDto response = CommonResDto.builder().message("해당 프로젝트 삭제가 완료되었습니다.").build();

		when(projectService.deleteProject(any())).thenReturn(response);

		ResponseEntity<CommonResDto> result = projectController.projectRemove(request);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(response, result.getBody());

	}

	@Test
	void 프로젝트_마감_요청_테스트() {
		ProjectNameReqDto request = ProjectNameReqDto.builder()
			.userNickName("plpl")
			.name("스마트 시티 TF")
			.build();

		CommonResDto response = CommonResDto.builder().message("프로젝트 마감 요청을 완료하였습니다.").build();

		when(projectService.requestProjectClose(any())).thenReturn(response);

		ResponseEntity<CommonResDto> result = projectController.projectCloseRequest(request);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(response, result.getBody());

	}

	@Test
	void 프로젝트_마감_승인_테스트() {
		CloseReqDto request = CloseReqDto.builder()
			.isApprove("false")
			.name("스마트 시티 TF")
			.role("PILOT")
			.userNickName("plpl")
			.build();

		CommonResDto response;
		if (request.getIsApprove().equals("true")) {
			response = CommonResDto.builder().message("마감 요청을 승인하였습니다.").build();
		} else {
			response = CommonResDto.builder().message("마감 요청을 반려하였습니다.").build();
		}

		when(projectService.closeProjectApprove(any())).thenReturn(response);

		ResponseEntity<CommonResDto> result = projectController.projectCloseApprove(request);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(response, result.getBody());

	}

	@Test
	void 프로젝트_마감_테스트() {
		ProjectNameReqDto request = ProjectNameReqDto.builder()
			.name("스마트 시티 TF")
			.userNickName("plpl")
			.role("PILOT")
			.build();

		CommonResDto response = CommonResDto.builder().message("해당 프로젝트를 마감으로 변경하였습니다.").build();

		when(projectService.closeProject(anyString(), anyString())).thenReturn(response);

		ResponseEntity<CommonResDto> result = projectController.projectClose(request);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(response, result.getBody());

	}

	@Test
	void 프로젝트_목록_조회_테스트() {
		ProjectNameReqDto request = ProjectNameReqDto.builder()
			.name("스마트시티")
			.userNickName("plpl")
			.role("PILOT")
			.build();

		List<ProjectSummaryResDto> response = projectService.getProjectList(request);

		when(projectService.getProjectList(any())).thenReturn(response);

		ResponseEntity<List<ProjectSummaryResDto>> result = projectController.projectList(request);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(response, result.getBody());

	}

	@Test
	void 프로젝트_완수_요청_리스트_조회_테스트() {
		ProjectNameReqDto request = ProjectNameReqDto.builder()
			.name("스마트시티")
			.role("PILOT")
			.userNickName("plpl")
			.build();

		List<ProjectCloseResDto> response = projectService.getProjectReqList(request);

		when(projectService.getProjectReqList(any())).thenReturn(response);

		ResponseEntity<List<ProjectCloseResDto>> result = projectController.projectReqList(request);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(response, result.getBody());

	}

}