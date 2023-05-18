package com.jooany.letsdeal.controller.dto.response;

import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.model.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {
    private Long id;
    private String username;
    private UserRole userRole;

    public static UserJoinResponse fromUserDto(UserDto userDto){
        return new UserJoinResponse(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getUserRole()
        );
    }


}
