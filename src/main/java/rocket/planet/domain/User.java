package rocket.planet.domain;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rocket.planet.repository.jpa.ProfileRepository;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Table(name = "user")
public class User extends BaseTime {
	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "user_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(nullable = false)
	private String userPwd;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Column
	private boolean userLock;

	@Column(nullable = false)
	private LocalDate userAccessDt;

	@Column(nullable = false)
	private String userName;

	@Column(nullable = false, unique = true)
	private String userId;

	@OneToMany(mappedBy = "user")
	private List<UserProject> userProject;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "auth_uid")
	private Authority authority;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "profile_uid")
	private Profile profile;

	@OneToMany(mappedBy = "user")
	private List<Org> belong;

	@Override
	public String toString() {
		return "유저{" +
			"유저 uuid=" + id +
			", 유저 잠금 여부=" + userLock +
			", 유저 최근 접속일=" + userAccessDt +
			", 유저이름='" + userName + '\'' +
			", 유저 id='" + userId + '\'' +
			'}';
	}

	public User updateProfile(Profile profile) {
		this.profile = profile;
		return this;
	}

}
