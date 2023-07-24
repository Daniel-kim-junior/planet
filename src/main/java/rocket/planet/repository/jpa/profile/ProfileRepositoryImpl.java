package rocket.planet.repository.jpa.profile;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import rocket.planet.domain.Profile;
import rocket.planet.domain.QCertification;
import rocket.planet.domain.QOrg;
import rocket.planet.domain.QPjtRecord;
import rocket.planet.domain.QProfile;
import rocket.planet.domain.QProfileAuthority;
import rocket.planet.domain.QProfileTech;
import rocket.planet.domain.QUserProject;
import rocket.planet.domain.Role;

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

	Role excludedRole = Role.ADMIN;

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
			.where(qProfile.profileStatus.eq(true), qProfile.userNickName.eq(userNickName))
			.fetch();
		return profiles.stream().findFirst();

	}

	@Override
	public List<Profile> selectProfilesBySearchKeyword(String keyword) {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		String replacedKeyword = keyword.replaceAll("\\s+", "");
		List<Profile> equalsSearch = queryFactory.selectFrom(qProfile)
			.where(
				qProfile.profileStatus.eq(true),
				qProfile.role.ne(Expressions.constant(excludedRole)),
				qProfile.userNickName.equalsIgnoreCase(replacedKeyword)
					.or(qProfile.org.any().department.deptName.eq(replacedKeyword))
					.or(qProfile.org.any().team.teamName.eq(replacedKeyword))
					.or(qProfile.userProject.any().project.projectName.eq(replacedKeyword))
					.or(qProfile.profileTech.any().tech.techName.equalsIgnoreCase(replacedKeyword))
			)
			.fetch();

		List<Profile> searchProfiles;

		if (equalsSearch.isEmpty()) {
			List<Profile> startsWithSearch = queryFactory.selectFrom(qProfile)
				.where(
					qProfile.profileStatus.eq(true),
					qProfile.role.ne(Expressions.constant(excludedRole)),
					qProfile.userNickName.startsWithIgnoreCase(replacedKeyword)
						.or(qProfile.org.any().department.deptName.containsIgnoreCase(replacedKeyword))
						.or(qProfile.org.any().team.teamName.containsIgnoreCase(replacedKeyword))
						.or(qProfile.userProject.any().project.projectName.containsIgnoreCase(replacedKeyword))
						.or(qProfile.profileTech.any().tech.techName.startsWithIgnoreCase(replacedKeyword))
				)
				.fetch();

			searchProfiles = startsWithSearch;
		} else {
			searchProfiles = equalsSearch;
		}

		Set<Profile> distinctProfiles = new LinkedHashSet<>(searchProfiles);
		return new ArrayList<>(distinctProfiles);

	}

}






