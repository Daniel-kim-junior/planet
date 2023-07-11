package rocket.planet.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public class ProfileDto {
    @Getter
    @Builder
    public static class ProfileReqDto {
        private String userId;
        private String userNickName;
        private String role;
        private boolean profileDisplay;
        private int profileCareer;
        private LocalDate profileBirthDt;
        private LocalDate profileStartDate;
        private boolean profileAnnualStatus;

        private List<OrgReqDto> org;
        private List<InsideProjectReqDto> userProject;
        private List<ProfileTechReqDto> profileTech;
        private List<OutsideProjectReqDto> extPjtRecord;
        private List<CertReqDto> certification;
    }

    @Getter
    @Builder
    public static class CertReqDto {
        private String certName;
        private LocalDate certDt;
        private String certAgency;
        private LocalDate certExpireDate;
        private String certType;
        private String certNumber;
    }

    @Getter
    @Builder
    public static class ProfileTechReqDto {
        private String techName;
    }

    @Getter
    @Builder
    public static class InsideProjectReqDto {
        private String projectName;
        private String projectDesc;
        private LocalDate projectStartDt;
        private LocalDate projectEndDt;
    }

    @Getter
    @Builder
    public static class OrgReqDto {
        private boolean orgStatus;
        private String deptName;
        private String teamName;
    }
    @Getter
    @Builder
    public static class OutsideProjectReqDto {
        private String pjtName;
        private String pjtDesc;
        private LocalDate pjtStartDt;
        private LocalDate pjtEndDt;
        private String pjtTech;
        private String pjtUserTech;

    }



}
