package com.example.travelmediarest.controller;

import com.example.travelmediarest.dto.RegistrationDto;
import com.example.travelmediarest.dto.UserDto;
import com.example.travelmediarest.repository.LocationRepository;
import com.example.travelmediarest.repository.PostRepository;
import com.example.travelmediarest.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    public static MockMvc mockMvc;
//    public static RegistrationDto registrationDto;

    @BeforeAll
    public static void init(@Autowired WebApplicationContext context, @Autowired LocationRepository locationRepository,
                            @Autowired PasswordEncoder passwordEncoder, @Autowired PostRepository postRepository, @Autowired UserRepository userRepository) {

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

    }

    private static String asJsonString(RegistrationDto registrationDto) {
        try {
            log.info("new ObjectMapper().writeValueAsString(post): : " + new ObjectMapper().writeValueAsString(registrationDto));
            return new ObjectMapper().writeValueAsString(registrationDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String asJsonString(UserDto userDto) {
        try {
            log.info("new ObjectMapper().writeValueAsString(post): : " + new ObjectMapper().writeValueAsString(userDto));
            return new ObjectMapper().writeValueAsString(userDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @WithMockUser("a@a.a")
    @Test
    public void registrationHandlerTest() throws Exception {
        RegistrationDto registrationDto = new RegistrationDto();

        registrationDto.setMail("a@a.a");
        registrationDto.setUsername("Himon");
        registrationDto.setPassword("h");
        registrationDto.setConfirm("h");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/register")
                        .content(asJsonString(registrationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.mail").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.role").value("ROLE_USER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.authorities[*].authority").value("ROLE_USER"));
    }

    @WithMockUser("a@a.a")
    @Test
    public void loginHandlerTest() throws Exception {
        UserDto userDto = new UserDto();

        userDto.setMail("a@a.a");
        userDto.setPassword("h");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/login")
                        .content(asJsonString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Jwt-token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Jwt-token").isNotEmpty());
    }
}