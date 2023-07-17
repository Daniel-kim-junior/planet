package rocket.planet.dto.profile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

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
        private List<ClosedInsideProjectResDto> userClosedProject;
        private List<UserInProgressProjectResDto> userInProgressProject;
        private List<ProfileTechResDto> profileTech;
        private List<OutsideProjectResDto> extPjtRecord;
        private List<CertResDto> certification;
        private List<VisitorResDto> visitor;
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
    public static class UserInProgressProjectResDto {
        private String projectName;
        private LocalDate userPjtCloseDt;
    }
    @Getter
    @Builder
    public static class CertResDto {
        private UUID certUid;
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
        private UUID userTechId;
        private String techName;
    }

    @Getter
    @Builder
    public static class ClosedInsideProjectResDto {
        private String projectName;
        private String projectDesc;
        private LocalDate userPjtJoinDt;
        private LocalDate userPjtCloseDt;
        private String userPjtDesc;
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
        private UUID pjtUid;
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
    public static class TechRegisterReqDto {
        private String userNickName;
        private String techName;
    }
    @Getter
    @Builder
    public static class insideProjectUpdateReqDto {
        private String userNickName;
        private String projectName;
        private String projectDesc;
    }

    @Getter
    @Builder
    public static class UserNewPwdReqDto{
        private String userId;
        private String userPwd;
        private String userPwdCheck;
    }

    @Getter
    @Builder
    public static class VisitorReqDto{
        private String visitorNickName;
        private String ownerNickName;
    }

    @Getter
    @Builder
    public static class VisitorResDto{
        private String visitorNickName;
        private String visitorRole;
    }


}



