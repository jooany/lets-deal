package com.jooany.letsdeal.controller.dto;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jooany.letsdeal.model.entity.User;
import com.jooany.letsdeal.model.enumeration.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements UserDetails {
	private Long id;
	private String username;
	private String password;
	private UserRole userRole;
	private String nickname;
	private Timestamp registeredAt;
	private Timestamp updateAt;
	private Timestamp deletedAt;

	public static UserDto from(User user) {
		return new UserDto(
			user.getId(),
			user.getUserName(),
			user.getPassword(),
			user.getUserRole(),
			user.getNickname(),
			user.getRegisteredAt(),
			user.getUpdateAt(),
			user.getDeletedAt()
		);
	}

	public void setNullPassword() {
		this.password = null;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(this.getUserRole().toString()));
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return this.deletedAt == null;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return this.deletedAt == null;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return this.deletedAt == null;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return this.deletedAt == null;
	}
}
