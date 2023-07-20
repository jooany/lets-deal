//package com.jooany.letsdeal.service;
//
//import com.jooany.letsdeal.exception.ErrorCode;
//import com.jooany.letsdeal.exception.LetsDealAppException;
//import com.jooany.letsdeal.fixture.entity.EntityFixture;
//import com.jooany.letsdeal.fixture.entity.UserFixture;
//import com.jooany.letsdeal.model.entity.User;
//import com.jooany.letsdeal.repository.UserRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//public class UserServiceTest {
//    @Autowired
//    private UserService userService;
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @MockBean
//    private BCryptPasswordEncoder encoder;
//
//    @Test
//    void 회원가입_성공_회원정보저장(){
//        String userName = "userName";
//        String password = "password";
//
//        User fixture = EntityFixture.createUser(userName, password, 1L);
//
//        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
//        when(encoder.encode(password)).thenReturn("encrypt_password");
//        when(userRepository.save(any())).thenReturn(UserFixture.get(userName, password, 1L));
//
//        Assertions.assertDoesNotThrow(() -> userService.join(userName, password));
//    }
//
//    @Test
//    void 회원가입_실패_사용자ID중복(){
//        String userName = "userName";
//        String password = "password";
//
//        User fixture = UserFixture.get(userName, password, 1L);
//
//        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
//        when(encoder.encode(password)).thenReturn("encrypt_password");
//        when(userRepository.save(any())).thenReturn(Optional.empty());
//
//        LetsDealAppException e = Assertions.assertThrows(LetsDealAppException.class, () -> userService.join(userName, password));
//        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_NAME, e.getErrorCode());
//    }
//
//    @Test
//    void 로그인_성공(){
//        String userName = "userName";
//        String password = "password";
//
//        User fixture = UserFixture.get(userName, password, 1L);
//
//        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
//        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);
//
//        Assertions.assertDoesNotThrow(() -> userService.login(userName, password));
//    }
//
//    @Test
//    void 로그인_실패_존재하지않는사용자(){
//        String userName = "userName";
//        String password = "password";
//
//        User fixture = UserFixture.get(userName, password, 1L);
//
//        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
//        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);
//
//        LetsDealAppException e = Assertions.assertThrows(LetsDealAppException.class, () -> userService.login(userName, password));
//        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
//
//    }
//
//    @Test
//    void 로그인_실패_유효하지않은패스워드(){
//        String userName = "userName";
//        String password = "password";
//
//        User fixture = UserFixture.get(userName, password, 1L);
//
//        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
//        when(encoder.matches(password, fixture.getPassword())).thenReturn(false);
//
//        LetsDealAppException e = Assertions.assertThrows(LetsDealAppException.class, () -> userService.login(userName, password));
//        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
//    }
//
//    @Test
//    void 토큰재발급_성공_토큰생성(){
//        String userName = "userName";
//
//        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(mock(User.class)));
//
//        Assertions.assertDoesNotThrow(() -> userService.generateTokens(userName));
//    }
//
//    @Test
//    void 회원탈퇴_성공(){
//        String userName = "userName";
//        String password = "password";
//
//        User fixture = UserFixture.get(userName, password, 1L);
//
//        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
//
//        Assertions.assertDoesNotThrow(() -> userService.delete(userName));
//    }
//
//}
