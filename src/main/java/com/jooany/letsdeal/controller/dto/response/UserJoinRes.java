package com.jooany.letsdeal.controller.dto.response;

import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.model.enumeration.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinRes {
	private Long id;
	private String userName;
	private UserRole userRole;

	public static UserJoinRes fromUserDto(UserDto userDto) {
		return new UserJoinRes(
			userDto.getId(),
			userDto.getUsername(),
			userDto.getUserRole()
		);
	}

}
