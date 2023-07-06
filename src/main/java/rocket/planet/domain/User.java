package rocket.planet.domain;

import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "user")
public class User extends BaseTime {
	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "user_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@OneToOne
	@JoinColumn(name = "profile_uid")
	private Profile profile;

	@Column(nullable = false)
	private String userPwd;

	@Column
	private boolean userLock;

	@Column(nullable = false)
	private LocalDate userAccessDt;

	@Column(nullable = false, unique = true)
	private String userId;

	@Builder
	public User(Profile profile, String userPwd, boolean userLock, LocalDate userAccessDt,
		String userId) {
		this.profile = profile;
		this.userPwd = userPwd;
		this.userLock = userLock;
		this.userAccessDt = userAccessDt;
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "유저{" +
			"유저 uuid=" + id +
			", 유저 잠금 여부=" + userLock +
			", 유저 최근 접속일=" + userAccessDt +
			", 유저 id='" + userId + '\'' +
			", 유저 잠금=" + userLock +
			", 유저 최근 접속일=" + userAccessDt +
			", 유저 id='" + userId + '\'' +
			'}';
	}

	public User updateProfile(Profile profile) {
		this.profile = profile;
		return this;
	}
}
