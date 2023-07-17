package rocket.planet.repository.jpa.profile;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocket.planet.domain.*;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepositoryCustom {

    private final EntityManager entityManager;
    private final QProfile qProfile = QProfile.profile;
    private final QProfileTech qProfileTech = QProfileTech.profileTech;
    private final QOrg qOrg = QOrg.org;
    private final QTech qTech = QTech.tech;
    private final QCertification qCertification = QCertification.certification;
    private final QUserProject qUserProject = QUserProject.userProject;
    private final QProfileAuthority qProfileAuthority = QProfileAuthority.profileAuthority;
    private final QPjtRecord qPjtRecord = QPjtRecord.pjtRecord;
    private final QProject qProject = QProject.project;



    @Override
    public Optional<Profile> selectProfileByUserNickName(String userNickName) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        List<Profile> profiles = queryFactory.selectFrom(qProfile)
                .leftJoin(qProfile.org, qOrg)
                .leftJoin(qProfile.authority, qProfileAuthority)
                .leftJoin(qProfile.userProject, qUserProject)
                .leftJoin(qProfile.extPjtRecord, qPjtRecord)
                .leftJoin(qProfile.certification, qCertification)
                .leftJoin(qProfile.profileTech, qProfileTech)
                .leftJoin(qProfileTech.tech, qTech)
                .leftJoin(qUserProject.project, qProject)
                .where(qProfile.userNickName.eq(userNickName))
                .fetch();
        return profiles.stream().findFirst();












    }
}


