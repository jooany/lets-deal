package com.jooany.letsdeal.model.entity;

import com.jooany.letsdeal.model.enumeration.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
@SequenceGenerator(
        name = "USERS_SEQ_GENERATOR",
        sequenceName = "USERS_SEQ"
)
public class User extends SoftDeletableBaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ_GENERATOR")
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    @Builder(builderMethodName = "userBuilder")
    private User(String userName, String password, String nickname, UserRole userRole) {
        this.userName = userName;
        this.password = password;
        this.nickname = nickname;
        this.userRole = (userRole != null) ? userRole : UserRole.USER;
    }

    public static UserBuilder builder(String userName, String password, String nickname) {
        return userBuilder()
                .userName(userName)
                .password(password)
                .nickname(nickname);
    }

    public void updatePw(String password) {
        this.password = password;
    }

    public void updateNick(String nickname) {
        this.nickname = nickname;
    }
}