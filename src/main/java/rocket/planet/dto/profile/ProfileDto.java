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
    public static class ProfileResDto {
        private String userId;
        private String userNickName;
        private String role;
        private boolean profileDisplay;
        private int profileCareer;
        private LocalDate profileBirthDt;
        private LocalDate profileStartDate;
        private boolean profileAnnualStatus;

        private OrgResDto org;
        private List<InsideProjectResDto> userProject;
        private List<ProfileTechResDto> profileTech;
        private List<OutsideProjectResDto> extPjtRecord;
        private List<CertResDto> certification;
    }
    @Getter
    @Builder
    public static class ProfileUpDateResDto {
        private String userName;
        private String userNickName;
        private boolean profileDisplay;
        private int profileCareer;
        private LocalDate profileBirthDt;
        private LocalDate profileStartDate;
        private boolean profileAnnualStatus;
    }
    @Getter
    @Builder
    public static class ProfileDisplayUpDateResDto {
        private String userNickName;
        private boolean profileDisplay;
    }
    @Getter
    @Builder
    public static class AnnualUpDateResDto {
        private String userNickName;
        private boolean profileAnnualStatus;
    }

    @Getter
    @Builder
    public static class CertResDto {
        private String certName;
        private LocalDate certDt;
        private String certAgency;
        private LocalDate certExpireDate;
        private String certType;
        private String certNumber;
    }

    @Getter
    @Builder
    public static class ProfileTechResDto {
        private String techName;
    }

    @Getter
    @Builder
    public static class InsideProjectResDto {
        private String projectName;
        private String projectDesc;
        private LocalDate projectStartDt;
        private LocalDate projectEndDt;
    }

    @Getter
    @Builder
    public static class OrgResDto {
        private boolean orgStatus;
        private boolean belongStatus;
        private String deptName;
        private String teamName;
    }
    @Getter
    @Builder
    public static class OutsideProjectResDto {
        private String pjtName;
        private String pjtDesc;
        private LocalDate pjtStartDt;
        private LocalDate pjtEndDt;
        private String pjtTech;
        private String pjtUserTech;

    }

    @Getter
    @Builder
    public static class OutsideProjectRegisterReqDto {
        private String userNickName;
        private String pjtName;
        private String pjtDesc;
        private LocalDate pjtStartDt;
        private LocalDate pjtEndDt;
        private String pjtTech;
        private String pjtUserTech;
    }

    @Getter
    @Builder
    public static class OutsideProjectUpdateReqDto {
        private String userNickName;
        private String pjtName;
        private String pjtDesc;
        private LocalDate pjtStartDt;
        private LocalDate pjtEndDt;
        private String pjtTech;
        private String pjtUserTech;
    }

    @Getter
    @Builder
    public static class CertRegisterResDto {
        private String userNickName;
        private String certName;
        private String certAgency;
        private String certType;
        private String certNumber;
        private LocalDate certDt;
        private LocalDate certExpireDate;
    }

    @Getter
    @Builder
    public static class CertUpdateResDto {
        private String userNickName;
        private String certName;
        private String certAgency;
        private String certType;
        private String certNumber;
        private LocalDate certDt;
        private LocalDate certExpireDate;
    }




}
