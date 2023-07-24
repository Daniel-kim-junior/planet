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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "profile_visitor")
@NoArgsConstructor(access = PROTECTED)
public class ProfileVisitor {
	@Id
	@GeneratedValue(generator = "uuid4")
	@GenericGenerator(name = "UUID", strategy = "uuid4")
	@Column(name = "visitor_uid", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(optional = false, fetch = LAZY)
	@JoinColumn(columnDefinition = "BINARY(16)", name = "profile_owner_uid", nullable = false)
	private Profile owner;

	@ManyToOne(optional = false, fetch = LAZY)
	@JoinColumn(columnDefinition = "BINARY(16)", name = "profile_visitor_uid", nullable = false)
	private Profile visitor;

	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDate visitTime;

	@Builder
	public ProfileVisitor(Profile owner, Profile visitor, LocalDate visitTime) {
		this.owner = owner;
		this.visitor = visitor;
		this.visitTime = visitTime;
	}

	@Override
	public String toString() {
		return "ProfileVisitor{" +
			"프로필 주인장 =" + owner +
			", 프로필 방문객 =" + visitor +
			", 방문시간 =" + visitTime +
			'}';
	}

	public void updateVisitTime() {
		this.visitTime = LocalDate.now();
	}

}

