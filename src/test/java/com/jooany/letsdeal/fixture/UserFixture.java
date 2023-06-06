package com.jooany.letsdeal.fixture;

import com.jooany.letsdeal.model.entity.User;

public class UserFixture {
    public static User get(String userName, String password, Long userId) {
        User result = new User();
        result.setId(userId);
        result.setUserName(userName);
        result.setPassword(password);
        return result;
    }
}
