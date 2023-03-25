package com.example.travelmediarest.service;

import com.example.travelmediarest.dto.RegistrationDto;
import com.example.travelmediarest.dto.UserDto;
import com.example.travelmediarest.model.User;
import com.example.travelmediarest.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
//@RunWith(JUnit4.class)
@SpringBootTest
@Slf4j
class DefaultUserDetailServiceTest {

//    private List<String> list;

    @Autowired
    UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    public static void registerUser(@Autowired UserRepository userRepository, @Autowired PasswordEncoder passwordEncoder) {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setMail("h");
        registrationDto.setUsername("h");
        registrationDto.setPassword("h");
        registrationDto.setConfirm("h");
        userRepository.saveAndFlush(registrationDto.toUser(passwordEncoder));
        log.info("register successfully");
    }

    @Test
//    @Disabled
    void registrationSaveTest() {
//        Mockito.when(userService.isAlreadyOrSaveUSer(any(RegistrationDto.class))).thenReturn(new User("test", "test", "test"));

        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setMail("a");
        registrationDto.setUsername("a");
        registrationDto.setPassword("a");
        registrationDto.setConfirm("a");
        userRepository.saveAndFlush(registrationDto.toUser(passwordEncoder));

        User savedUser;
//        = userService.isAlreadyOrSaveUSer(registrationDto);
        savedUser = userService.isAlreadyOrSaveUSer(registrationDto);
        assertThat(savedUser).isNull();

        userRepository.deleteAll();

        savedUser = userService.isAlreadyOrSaveUSer(registrationDto);
        assertThat(savedUser).isNotNull();
//         assertEquals(savedUser,"");
    }

    @Test
//    @Disabled
    void authenticateByCredentialsTest() {
        UserDto userDto = new UserDto("h", "h", "h", "ROLE_USER");
        Map<String, Object> map = null;
        map = userService.authenticateByCredentials(userDto);

        log.info("map: " + map);
//        assertThat(map).isNotNull();
        assertNotNull(map);

        userDto.setMail("a");

        assertThrows(RuntimeException.class, () -> {
            userService.authenticateByCredentials(userDto);
        });

        userDto.setUsername("h");
        userDto.setPassword("a");

        assertThrows(RuntimeException.class, () -> {
            userService.authenticateByCredentials(userDto);
        });

        //FIRST WAY  FOR asserThrows
        userDto.setPassword("h");
        userDto.setRole("ROLE_ADMIN");
        assertThrows(RuntimeException.class, () -> {
            userService.authenticateByCredentials(userDto);
        });

        //SECOND WAY  FOR asserThrows
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.authenticateByCredentials(userDto);
        });

        String expectedMessage = "invalid login Credentials";
        String actualMessage = exception.getMessage();


        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
//    @Disabled
    void fetchUserByMailTest() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setMail("b");
        registrationDto.setUsername("b");
        registrationDto.setPassword("b");
        registrationDto.setConfirm("b");
        userRepository.saveAndFlush(registrationDto.toUser(passwordEncoder));
        String mail = "b";

        User expected = new User("b","b","b");
        User response = userService.fetchUserByMail(mail);
        response.setId(null);
        assertEquals(passwordEncoder.matches("b",response.getPassword()),true );
        response.setPassword("b");
        log.info("response :"+response);
        log.info("expected :"+expected);
        assertEquals(expected,response);
    }
}