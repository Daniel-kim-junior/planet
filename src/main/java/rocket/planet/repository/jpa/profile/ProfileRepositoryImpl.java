package rocket.planet.repository.jpa.profile;

import antlr.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocket.planet.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepositoryCustom {

    private final EntityManager entityManager;
    private final QProfile qProfile = QProfile.profile;
    private final QProfileTech qProfileTech = QProfileTech.profileTech;
    private final QOrg qOrg = QOrg.org;

    private final QCertification qCertification = QCertification.certification;
    private final QUserProject qUserProject = QUserProject.userProject;
    private final QProfileAuthority qProfileAuthority = QProfileAuthority.profileAuthority;
    private final QPjtRecord qPjtRecord = QPjtRecord.pjtRecord;


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
                .where(qProfile.userNickName.eq(userNickName))
                .fetch();
        return profiles.stream().findFirst();

    }

    @Override
    public List<Profile> selectProfilesBySearchKeyword(String keyword) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        String replacedKeyword = keyword.replaceAll("\\s+", "");

        List<Profile> searchProfiles = queryFactory.selectFrom(qProfile)
            .where(
            qProfile.userName.containsIgnoreCase(replacedKeyword)
                    .or(qProfile.userNickName.equalsIgnoreCase(replacedKeyword))
            .or(qProfile.org.any().department.deptName.containsIgnoreCase(replacedKeyword))
            .or(qProfile.org.any().team.teamName.containsIgnoreCase(replacedKeyword))
            .or(qProfile.userProject.any().project.projectName.containsIgnoreCase(replacedKeyword))
                .or(qProfile.profileTech.any().tech.techName.equalsIgnoreCase(replacedKeyword)
                        .or(qProfile.profileTech.any().tech.techName.startsWithIgnoreCase(replacedKeyword))
                ))
            .fetch();

        List<Profile> distinctProfiles = searchProfiles.stream()
                .distinct()
                .collect(Collectors.toList());
        return distinctProfiles;

        }


    }






