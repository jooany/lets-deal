package com.jooany.letsdeal.fixture;

import com.jooany.letsdeal.model.entity.UserEntity;

public class UserEntityFixture {
    public static UserEntity get(String userName, String password, Long userId) {
        UserEntity result = new UserEntity();
        result.setId(userId);
        result.setUserName(userName);
        result.setPassword(password);
        return result;
    }
}
