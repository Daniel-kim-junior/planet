package rocket.planet.domain;

import static javax.persistence.FetchType.*;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "profile_uid")
	private Profile profile;

	@Column(nullable = false)
	private String userPwd;

	@Column
	private boolean userLock;

	@Column
	private LocalDate lastPwdModifiedDt;

	@Column(nullable = false, unique = true)
	private String userId;

	@Builder
	public User(Profile profile, String userPwd, boolean userLock,
		String userId, LocalDate lastPwdModifiedDt) {
		this.profile = profile;
		this.lastPwdModifiedDt = lastPwdModifiedDt;
		this.userPwd = userPwd;
		this.userLock = userLock;
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "유저{" +
			"유저 uuid=" + id +
			", 유저 잠금 여부=" + userLock +
			", 유저 id='" + userId + '\'' +
			", 유저 잠금=" + userLock +
			", 유저 비밀번호 변경 날짜=" + lastPwdModifiedDt +
			'}';
	}

	public User updateProfile(Profile profile) {
		this.profile = profile;
		return this;
	}

	public User updatePassword(String userPwd) {
		this.userPwd = userPwd;
		return this;
	}

	public static User defaultUser(String userId, String userPwd) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return builder().userLock(false)
			.lastPwdModifiedDt(LocalDate.now())
			.userId(userId).userPwd(passwordEncoder.encode(userPwd)).build();
	}

	public boolean isExistProfile() {
		return this.profile != null;
	}
}
