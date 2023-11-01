package com.jooany.letsdeal.model.entity;

import java.sql.Timestamp;
import java.time.Instant;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.jooany.letsdeal.model.enumeration.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "\"user\"")
@SequenceGenerator(
	name = "USER_SEQ_GENERATOR",
	sequenceName = "USER_SEQ",
	initialValue = 1, allocationSize = 50
)
@SQLDelete(sql = "UPDATE \"user\" SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is NULL")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "password")
	private String password;

	@Column(name = "nickname")
	private String nickname;

	@Builder.Default
	@Column(name = "user_role")
	@Enumerated(EnumType.STRING)
	private UserRole userRole = UserRole.USER;

	@Column(name = "registered_at")
	private Timestamp registeredAt;

	@Column(name = "updated_at")
	private Timestamp updateAt;

	@Column(name = "deleted_at")
	private Timestamp deletedAt;

	@PrePersist
	void registerdAt() {
		this.registeredAt = Timestamp.from(Instant.now());
	}

	@PreUpdate
	void updatedAt() {
		this.updateAt = Timestamp.from(Instant.now());
	}

	public static User of(String userName, String password, String nickname) {
		return User.builder()
			.userName(userName)
			.password(password)
			.nickname(nickname)
			.build();
	}

	public void updatePw(String password) {
		this.password = password;
	}

	public void updateNick(String nickname) {
		this.nickname = nickname;
	}
}